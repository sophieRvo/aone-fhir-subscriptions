# Setting Up Team Repository

## Overview
Since you cloned directly from athenahealth's repository, we'll set up a separate remote for your team's changes while keeping the original as a reference.

## Step 1: Create a New Repository

1. Go to GitHub/GitLab/Bitbucket and create a **new private repository**
   - Name it something like: `aone-fhir-subscriptions-team` or `aone-fhir-subscriptions-custom`
   - Make it **private** (so only your team can access it)
   - **DO NOT** initialize it with a README, .gitignore, or license (we already have these)

2. Copy the repository URL (e.g., `https://github.com/your-org/aone-fhir-subscriptions-team.git`)

## Step 2: Add Your Team Repository as a Remote

Run these commands (replace `YOUR_REPO_URL` with your actual repository URL):

```bash
cd /Users/slikit/code/aone-fhir-subscriptions

# Add your team repository as a new remote called "team"
git remote add team YOUR_REPO_URL

# Verify remotes (you should see both "origin" and "team")
git remote -v
```

## Step 3: Commit Your Changes

```bash
# Stage all your changes
git add .

# Commit with a descriptive message
git commit -m "Add team-specific improvements: event viewing endpoints, scope fixes, and documentation updates"
```

## Step 4: Push to Your Team Repository

```bash
# Push to your team repository
git push team main

# Or if you want to use a different branch name:
git checkout -b team-main
git push team team-main
```

## Step 5: Share with Your Team

1. Give your teammates access to the new repository
2. They can clone it with:
   ```bash
   git clone YOUR_REPO_URL
   ```

## Optional: Keep Up with Original Repository

To pull updates from the original athenahealth repository:

```bash
# Pull updates from athenahealth
git fetch origin

# Merge updates into your branch (if needed)
git merge origin/main
```

## Recommended Workflow Going Forward

1. **Keep `origin` pointing to athenahealth** - for pulling upstream updates
2. **Use `team` remote** - for pushing your team's changes
3. **Create feature branches** - for new work:
   ```bash
   git checkout -b feature/your-feature-name
   # Make changes
   git commit -m "Description"
   git push team feature/your-feature-name
   ```

## Alternative: Rename Remotes (Optional)

If you prefer to have your team repo as "origin":

```bash
# Rename current origin to upstream
git remote rename origin upstream

# Add your team repo as origin
git remote add origin YOUR_REPO_URL

# Now "origin" is your team repo, "upstream" is athenahealth
```
