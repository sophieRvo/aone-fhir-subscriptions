# Converting Repository to Fork - Step by Step Guide

## Important Note
GitHub doesn't allow converting an existing repository into a fork. We'll create a new fork and migrate your changes.

## Step 1: Create a Fork on GitHub

1. Go to https://github.com/athenahealth/aone-fhir-subscriptions
2. Click the **"Fork"** button in the top right
3. This will create a new repository: `https://github.com/sophieRvo/aone-fhir-subscriptions` (if available) or `https://github.com/sophieRvo/aone-fhir-subscriptions-fork`

**Note:** If the name `aone-fhir-subscriptions` is already taken by your current repo, GitHub will suggest a different name or you can rename your current repo first.

## Step 2: Rename Your Current Repository (Optional)

If you want to keep your current repo for reference:

1. Go to your current repo: https://github.com/sophieRvo/aone-fhir-subscriptions
2. Go to **Settings** → **General** → Scroll down to **Repository name**
3. Rename it to something like: `aone-fhir-subscriptions-team` or `aone-fhir-subscriptions-backup`
4. This frees up the name for the fork

## Step 3: Create the Fork

1. Go back to https://github.com/athenahealth/aone-fhir-subscriptions
2. Click **"Fork"**
3. Select your account (sophieRvo)
4. The fork will be created at: `https://github.com/sophieRvo/aone-fhir-subscriptions`

## Step 4: Update Your Local Repository

After creating the fork, run these commands locally:

```bash
cd /Users/slikit/code/aone-fhir-subscriptions

# Add the fork as a new remote (temporarily)
git remote add fork https://github.com/sophieRvo/aone-fhir-subscriptions.git

# Fetch the fork
git fetch fork

# Push your team's changes to the fork
git push fork main

# Update your remotes
git remote remove team  # Remove old team remote
git remote rename origin upstream  # Rename athenahealth to upstream
git remote rename fork origin  # Make fork the new origin

# Verify
git remote -v
# Should show:
# origin -> your fork
# upstream -> athenahealth's repo
```

## Step 5: Update Your Team

If you renamed your old repo, your team will need to update their remotes:

```bash
# In their local repos
git remote set-url origin https://github.com/sophieRvo/aone-fhir-subscriptions.git
git fetch origin
```

## Benefits of Having a Fork

- Shows as a fork on GitHub (visual relationship)
- Can create Pull Requests back to athenahealth easily
- GitHub tracks the fork relationship
- Easier to sync with upstream changes
- Can see how many commits ahead/behind upstream
