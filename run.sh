#!/bin/bash

getTestParameters() {
    echo ""
    echo "Please enter the test group ( LOGIN, GRID, SEARCH, CHECKOUT )"
    read -r group

    echo "Please enter the browser"
    echo "Chrome, Firefox, Edge"
    echo "( default: chrome )   press Enter to skip..."
    read -r browser
}

checkSeleniumHub() {
  SELENIUM_HUB_URL="http://localhost:4444/wd/hub/status"
    response=$(curl -s -o /dev/null -w "%{http_code}" $SELENIUM_HUB_URL)
    if [ $response -eq 200 ]; then
        echo "Selenium Hub is running and ready for testing..."
        sleep 8
        return 0
    else
        echo "Selenium Hub is not running... waiting..."
        return 1
    fi
}

startDockerEnvironment() {
    export environment="docker"
    export HEADLESS="true"
    export NODE_REPLICAS=1

    if [ -z "$(docker-compose ps -q)" ]; then
        docker-compose up -d
    fi

    while ! docker inspect -f '{{.State.Running}}' selenium-hub &> /dev/null; do
        echo "Waiting for Selenium Hub to be up..."
        sleep 1
    done

    getTestParameters
}

echo "Do you want to run the tests in Docker or locally? ( docker/local )"
read -r env

shopt -s nocasematch
if [[ "$env" == "docker" ]]; then
    startDockerEnvironment

    while ! checkSeleniumHub -eq 0; do
        echo "Waiting for Selenium Hub to be up..."
        sleep 5
    done

    mvnArgs="clean compile test"

    [[ -n "$group" ]] && mvnArgs="$mvnArgs -Dgroup=$group"
    [[ -n "$browser" ]] && mvnArgs="$mvnArgs -Dbrowser=$browser"

    mvn $mvnArgs

  docker-compose down

elif [[ "$env" == "local" ]]; then
  echo "Do you want to run the tests in Headless mode? ( yes/no )"
  read -r mode

  if [[ "$mode" == "yes" ]]; then
    export HEADLESS="true";
  fi

    getTestParameters

    mvnArgs="clean compile test"
    [[ -n "$group" ]] && mvnArgs="$mvnArgs -Dgroup=$group"
    [[ -n "$browser" ]] && mvnArgs="$mvnArgs -Dbrowser=$browser"

    mvn $mvnArgs

else
    echo "Invalid option. Please select 'docker' or 'local'."
    exit 1
fi
shopt -u nocasematch

