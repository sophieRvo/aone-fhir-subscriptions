# Setup Guide - Running the Athenahealth Event Subscription Sample

This guide will help you set up and run the Java webhook sample application.

## Prerequisites Check

✅ **Java 11** - You have Java 11 installed (verified: openjdk version "11.0.27")

✅ **Maven** - Installed via Homebrew (version 3.9.12)

**Important:** This project requires Java 11. If you have multiple Java versions installed, make sure to set `JAVA_HOME` to Java 11 before building or running.

## Step-by-Step Setup

### Step 1: Configure Athena Credentials

You need to edit the configuration file to add your Athena Client ID and Secret:

1. Open `src/main/resources/application.yaml`
2. Replace `<Client_Id>` with your actual Athena Client ID
3. Replace `<Client_Secret>` with your actual Athena Client Secret

**Note:** If you don't have credentials yet, you'll need to:
- Register at the [athenahealth Developer Portal](https://docs.athenahealth.com/api/guides/onboarding-overview)
- Request the following scopes from athenahealth API operations team:
  - `system/SubscriptionTopic.read`
  - `system/Subscription.write`
  - `system/Subscription.read`

### Step 2: Build the Project

Navigate to the Java project directory and build using Maven. **Important:** You must use Java 11 (not Java 25 or later).

**On macOS/Linux:**
```bash
cd /Users/slikit/code/aone-fhir-subscriptions/samplecode/java
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
mvn clean install
```

**Note:** If tests fail, you can skip them for now:
```bash
mvn clean install -DskipTests
```

**On Windows:**
```bash
cd samplecode\java
set JAVA_HOME=<path-to-java-11>
mvn clean install
```

**Note:** If you don't have Maven installed, you can install it via Homebrew on macOS:
```bash
brew install maven
```

### Step 3: Run the Application

Run the application with the `local` profile. **Make sure JAVA_HOME is set to Java 11:**

**On macOS/Linux:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```

**On Windows (PowerShell):**
```powershell
$env:JAVA_HOME="<path-to-java-11>"
$env:SPRING_PROFILES_ACTIVE="local"
mvn spring-boot:run
```

Or if you prefer to run from your IDE:
- Set the environment variable: `SPRING_PROFILES_ACTIVE=local`
- Run the main class: `com.athenahealth.eventing.partner.PartnerWebhookApplication`

### Step 4: Verify the Service is Running

Once started, the service will run on **port 8080**. You can verify it's working by:

1. **Check Swagger UI**: Navigate to http://localhost:8080/swagger-ui/index.html
2. **Check Health Endpoint**: http://localhost:8080/actuator/health

### Step 5: Test the Webhook (Optional)

The project includes test scripts to send sample notifications:

```bash
./send-appointment-notification.sh
./send-patient-notification.sh
./send-order-notification.sh
```

**Note:** If you modify the notification payload in these scripts, you'll need to recalculate the `X-Hub-Signature` header. You can use an online HMAC calculator like https://dinochiesa.github.io/hmachash/index.html

## Configuration Details

- **Port**: 8080 (default Spring Boot port; can be configured in `application-local.yaml`)
- **Environment**: Preview (configured in `application.yaml`)
- **Base URL**: https://api.preview.platform.athenahealth.com

## Troubleshooting

### Maven Wrapper Issues

If `./mvnw` fails with errors about missing `.mvn` directory, use `mvn` directly instead:
```bash
# Use mvn instead of ./mvnw
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
mvn clean install -DskipTests
```

**Note:** Maven doesn't have a `build` phase. Use `mvn clean install` or `mvn clean package` instead of `mvn clean build`.

### Port Already in Use

If port 8080 is already in use, you can change it by editing `application-local.yaml`:
```yaml
server:
  port: 8888  # or any other available port
```

### Missing Credentials

If you see authentication errors, double-check that:
- Your Client ID and Secret are correctly set in `application.yaml`
- The credentials are for the Preview environment
- You have the necessary scopes granted

### Java Version Issues

If you see compilation errors related to Lombok or "cannot find symbol" errors:
- Make sure you're using Java 11 (not Java 17, 21, or 25)
- Set JAVA_HOME before building: `export JAVA_HOME=$(/usr/libexec/java_home -v 11)`
- Verify Java version: `java -version` should show version 11.x.x

## Next Steps

Once the service is running, you can:
1. Explore the API documentation at the Swagger UI
2. Set up your webhook endpoint to receive FHIR subscription notifications
3. Create subscriptions using the Athena API (see main README.md for details)
