#!/bin/bash

# Fix Java environment to avoid snap conflicts
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
unset LD_LIBRARY_PATH
unset LD_PRELOAD

MVN_BIN="apache-maven-3.9.6/bin"

echo "Checking for Maven..."
if [ ! -d "$MVN_BIN" ]; then
    echo "Maven not found in $MVN_BIN. Installing Maven..."
    
    # Check if Maven is installed globally
    if command -v mvn &> /dev/null; then
        echo "Using system Maven..."
        MVN_CMD="mvn"
    else
        echo "Maven not found. Please install Maven first:"
        echo "  sudo apt update"
        echo "  sudo apt install maven"
        exit 1
    fi
else
    MVN_CMD="$MVN_BIN/mvn"
fi

echo "Building Project..."
$MVN_CMD clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build Failed!"
    exit 1
fi

echo "Starting Application..."
$MVN_CMD exec:java -Dexec.mainClass="com.kiloux.restopos.Main"
