## 1 - Setting up the service locally

### 1.1 - Pre-requisites

- Java11
- Maven
- athenahealth developer portal preview client ID and secret

### 1.2 - Build and Run

**⚠️ Important:** You MUST use Java 11 before running Maven commands. The project requires Java 11 and will fail with newer Java versions.

**Using jenv (Recommended):**

If you use `jenv` to manage Java versions:

```bash
# Set Java 11 for this directory (local)
jenv local 11

# Set JAVA_HOME from jenv (required for Maven)
export JAVA_HOME=$(jenv javahome)

# Verify Java version
java -version  # Should show Java 11
echo $JAVA_HOME  # Should show jenv Java home path

# Build
mvn clean install -DskipTests

# Run
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```

**Note:** If you have jenv's `export` plugin enabled in your shell (check with `jenv plugins`), `JAVA_HOME` may be set automatically. If not, use `export JAVA_HOME=$(jenv javahome)` before running Maven.

**Using JAVA_HOME (Alternative):**

If you don't use `jenv`:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)  # macOS/Linux - ensure Java 11
mvn clean install -DskipTests

# Run
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```

**Note:** If you get compilation errors like `Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`, it means Maven is using the wrong Java version. Use `jenv local 11` or set `JAVA_HOME` before running Maven.

The service will start on `http://localhost:8080`

### 1.3 - Configuration

**Set up your credentials using environment variables:**

1. Copy the example environment file:

   ```bash
   cd samplecode/java
   cp .env.example .env
   ```

2. Edit the `.env` file and add your actual credentials:

   ```bash
   ATHENA_CLIENT_ID=your-actual-client-id
   ATHENA_CLIENT_SECRET=your-actual-client-secret
   ```

   **Important:** The `.env` file is ignored by git and will never be committed. This keeps your secrets safe.

3. Alternatively, you can set environment variables directly:
   ```bash
   export ATHENA_CLIENT_ID=your-client-id
   export ATHENA_CLIENT_SECRET=your-client-secret
   ```

**Required Scopes:**

The application requires the following OAuth scopes (request these from athenahealth API operations team):

- `athena/service/Athenanet.MDP.*` - For MDP API endpoints
- `system/SubscriptionTopic.read` - For reading subscription topics
- `system/Subscription.write` - For creating/updating subscriptions
- `system/Subscription.read` - For reading subscriptions
- `system/Patient.read` - For fetching Patient resources via FHIR (required for patient notifications)

**Note:** The scopes are already configured in `application.yaml`. If you need additional resource types (e.g., Appointment, Encounter), you may need to add corresponding FHIR scopes like `system/Appointment.read`, `system/Encounter.read`, etc.

### 1.4 - Running from IDE (Alternative)

Run the partner-service in your favorite IDE with the below environment variables:

```bash
SPRING_PROFILES_ACTIVE=local
```

Navigate to `http://localhost:8080/swagger-ui/index.html`

### 1.5 - Testing the service

Test webhook endpoint using the provided shell scripts:

```bash
./send-appointment-notification.sh
./send-patient-notification.sh
./send-order-notification.sh
```

**Verify notificationEvent:**

```bash
# Check event count
curl http://localhost:8080/events/count

# View all received events (pretty-printed with jq)
curl -s http://localhost:8080/events | jq .

# Save events to JSON file
curl -s http://localhost:8080/events | jq . > events.json

# View events without jq (if jq not installed)
curl -s http://localhost:8080/events

# Clear events (optional)
curl -X DELETE http://localhost:8080/events
```

Note: if you want to adjust the example notification payload provided in the above script, you will also need to update the X-Hub-Signature accordingly. _Whitespace matters here, including trailing whitespace._ To calculate the expected HMAC signature you can use an online calculator such as <https://dinochiesa.github.io/hmachash/index.html>. For example:

![hmac calculator screenshot](example-signature-calculation.png)

### 1.6 - Troubleshooting

**Java Version Mismatch (Build Failure):**
If you see `Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`, Maven is using the wrong Java version:

```bash
# Using jenv (recommended)
jenv local 11
mvn clean install -DskipTests

# Or using JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
mvn clean install -DskipTests
```

**Port 8080 already in use:**
If you see "Port 8080 was already in use", stop the existing process:

```bash
# Find and kill the process using port 8080
lsof -ti:8080 | xargs kill
# Or use a different port by editing application-local.yaml
```

**403 Forbidden when inflating payload:**

If you see `403 Forbidden` errors in the logs when processing notifications, this typically means the OAuth token doesn't have the required scopes for the resource type being fetched. Ensure you have:

- `system/Patient.read` for Patient notifications
- `system/Appointment.read` for Appointment notifications (if using FHIR references)
- `system/Encounter.read` for Encounter notifications (if using FHIR references)
- Other resource-specific scopes as needed

**Note:** The webhook endpoint will still return `200 OK` even if payload inflation fails - the error is logged but doesn't fail the webhook response. Check the application logs for details.
