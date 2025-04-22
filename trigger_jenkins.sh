#!/bin/bash

# Jenkins configuration
JENKINS_URL="http://54.221.128.107:8080"
JOB_NAME="Telus-SDET-Pipeline"
JENKINS_USER="admin"
API_TOKEN="11db5033b68ee8eec95a693118c0569d41"

# Get CSRF Crumb (required if CSRF protection is enabled)
CRUMB=$(curl -s -u "${JENKINS_USER}:${API_TOKEN}" \
  "${JENKINS_URL}/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,':',//crumb)")

# Trigger Jenkins job
curl -X POST \
  "${JENKINS_URL}/job/${JOB_NAME}/build" \
  --user "${JENKINS_USER}:${API_TOKEN}" \
  -H "$CRUMB"