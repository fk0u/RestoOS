$ErrorActionPreference = "Stop"

$projectRoot = $PSScriptRoot
Set-Location $projectRoot

Write-Host "Checking Apache Ant..."
$antCommand = Get-Command ant -ErrorAction SilentlyContinue

if (-not $antCommand) {
    Write-Host "Ant tidak ditemukan di PATH."
    Write-Host "Silakan install Apache Ant lalu jalankan ulang script ini."
    Write-Host "Download: https://ant.apache.org/bindownload.cgi"
    exit 1
}

Write-Host "Verifying Ant..."
ant -version

Write-Host "Building Project with Ant..."
ant clean copy-resources

if ($LASTEXITCODE -eq 0) {
    Write-Host "Running Application with Ant..."
    ant run
} else {
    Write-Host "Build Failed."
}
