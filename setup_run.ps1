$ErrorActionPreference = "Stop"

$mvnVersion = "3.9.6"
$mvnUrl = "https://archive.apache.org/dist/maven/maven-3/$mvnVersion/binaries/apache-maven-$mvnVersion-bin.zip"
$mvnFolder = "apache-maven-$mvnVersion"
$zipFile = "maven.zip"
$projectRoot = $PSScriptRoot

Write-Host "Checking Maven..."

if (-not (Test-Path "$projectRoot\$mvnFolder")) {
    Write-Host "Downloading Maven $mvnVersion..."
    Invoke-WebRequest -Uri $mvnUrl -OutFile "$projectRoot\$zipFile"
    
    Write-Host "Extracting Maven..."
    Expand-Archive -Path "$projectRoot\$zipFile" -DestinationPath "$projectRoot" -Force
    
    Remove-Item "$projectRoot\$zipFile"
    Write-Host "Maven Installed."
} else {
    Write-Host "Maven already present."
}

$env:PATH = "$projectRoot\$mvnFolder\bin;$env:PATH"

Write-Host "Verifying Maven..."
mvn -version

Write-Host "Building Project..."
mvn clean install

if ($LASTEXITCODE -eq 0) {
    Write-Host "Running Application..."
    # Use stop parsing symbol for PowerShell to pass arguments correctly
    mvn exec:java --% -Dexec.mainClass=com.kiloux.restopos.Main
} else {
    Write-Host "Build Failed."
}
