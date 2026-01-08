#!/bin/bash
# Script to convert your repository setup to use a fork

echo "Step 1: Adding fork as a remote..."
git remote add fork https://github.com/sophieRvo/aone-fhir-subscriptions.git

echo "Step 2: Fetching fork..."
git fetch fork

echo "Step 3: Pushing your changes to the fork..."
git push fork main

echo "Step 4: Updating remotes..."
# Rename origin to upstream (athenahealth's repo)
git remote rename origin upstream

# Remove old team remote (or keep it if you want)
# git remote remove team

# Make fork the new origin
git remote rename fork origin

echo "Step 5: Verifying setup..."
git remote -v

echo ""
echo "✅ Setup complete!"
echo ""
echo "Your remotes are now:"
echo "  origin    -> your fork (https://github.com/sophieRvo/aone-fhir-subscriptions)"
echo "  upstream  -> athenahealth's repo"
echo "  team      -> your old repo (if you kept it)"
echo ""
echo "To pull updates from athenahealth:"
echo "  git fetch upstream"
echo "  git merge upstream/main"
echo ""
echo "To push your changes:"
echo "  git push origin main"
