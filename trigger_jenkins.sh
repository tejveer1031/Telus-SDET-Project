#!/bin/bash
# Secure Jenkins job trigger script

# Configuration (store these in Jenkins credentials instead)
JENKINS_URL="http://52.73.89.216:8080"
JOB_NAME="Telus-SDET-Pipeline"
CREDENTIALS_ID="jenkins-trigger-creds"  # Store in Jenkins credentials store

# Get credentials from Jenkins environment
USER=$(cat "$JENKINS_SECRETS/${CREDENTIALS_ID}_USERNAME")
TOKEN=$(cat "$JENKINS_SECRETS/${CREDENTIALS_ID}_PASSWORD")

# Get CSRF Crumb using JSON API
CRUMB_JSON=$(curl -s -u "${USER}:${TOKEN}" "${JENKINS_URL}/crumbIssuer/api/json")
CRUMB_FIELD=$(jq -r '.crumbRequestField' <<< "$CRUMB_JSON")
CRUMB_VALUE=$(jq -r '.crumb' <<< "$CRUMB_JSON")

# Trigger job with validation
response=$(curl -s -w "%{http_code}" -o /dev/null \
  -X POST \
  -u "${USER}:${TOKEN}" \
  -H "${CRUMB_FIELD}: ${CRUMB_VALUE}" \
  "${JENKINS_URL}/job/${JOB_NAME}/build" \
  --data-urlencode json='{"parameter": []}')

# Handle response
case $response in
  201) echo "Job triggered successfully" ;;
  403) echo "ERROR: Authentication failed" >&2; exit 1 ;;
  404) echo "ERROR: Job not found" >&2; exit 1 ;;
  *)   echo "ERROR: Unknown response ($response)" >&2; exit 1 ;;
esac

# Optional: Wait for job completion (add if needed)
# while curl -s -u "${USER}:${TOKEN}" "${JENKINS_URL}/job/${JOB_NAME}/lastBuild/api/json" | jq -e '.building == true'; do
#   sleep 5
# done