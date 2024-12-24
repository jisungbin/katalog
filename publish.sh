#!/bin/bash

# Usage:
#   ./publish.sh          # Publishes to Maven Central
#   ./publish.sh local    # Publishes to Maven Local

set -e

PROJECTS=(
  ":librarian-runtime"
  ":librarian-processor"
)

COMMAND_SUFFIX="publishAndReleaseToMavenCentral"

if [ "$1" == "local" ]; then
  COMMAND_SUFFIX="publishToMavenLocal"
fi

GREEN='\033[0;32m'
NC='\033[0m' # No Color

for PROJECT in "${PROJECTS[@]}"; do
  echo -e "${GREEN}Executing: ./gradlew ${PROJECT}:${COMMAND_SUFFIX}${NC}"
  ./gradlew "${PROJECT}:${COMMAND_SUFFIX}" --console=plain --no-configuration-cache
done
