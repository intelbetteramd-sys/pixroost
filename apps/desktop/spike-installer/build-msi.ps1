# Spike S-07: builds Pixroost.msi from the Compose app image with our own main.wxs (firewall rule).
param(
    [Parameter(Mandatory)][string]$AppImage,
    [Parameter(Mandatory)][string]$ResourceDir,
    [Parameter(Mandatory)][string]$Output,
    [Parameter(Mandatory)][string]$JavaHome,
    [Parameter(Mandatory)][string]$Version
)
$ErrorActionPreference = 'Stop'

# jpackage looks for WiX on PATH: wix.exe (4+) or candle.exe/light.exe (3.x).
$wix3 = Get-ChildItem "${env:ProgramFiles(x86)}\WiX Toolset v3*\bin" -ErrorAction SilentlyContinue | Select-Object -First 1
if ($wix3) { $env:PATH = "$($wix3.FullName);$env:PATH" }
if (-not (Get-Command candle.exe -ErrorAction SilentlyContinue) -and -not (Get-Command wix.exe -ErrorAction SilentlyContinue)) {
    throw 'WiX Toolset not found (install WiX 3.14: choco install wixtoolset)'
}

# Only main.wxs goes into the resource dir, so nothing else overrides jpackage defaults.
New-Item -ItemType Directory -Force $Output | Out-Null
$resources = Join-Path $Output 'resources'
Remove-Item $resources -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force $resources | Out-Null
Copy-Item (Join-Path $ResourceDir 'main.wxs') $resources

$work = Join-Path $Output 'jpackage-temp'
Remove-Item $work -Recurse -Force -ErrorAction SilentlyContinue

& "$JavaHome\bin\jpackage.exe" --type msi --app-image $AppImage --dest $Output --temp $work `
    --name Pixroost --app-version $Version --vendor Pixroost `
    --description 'Pixroost installer spike' `
    --resource-dir $resources `
    --win-menu --win-shortcut --win-dir-chooser `
    --win-upgrade-uuid '7d1b6b1e-5c0a-4c3e-9a55-3c3d8d9f0a07'
if ($LASTEXITCODE -ne 0) { throw "jpackage failed with exit code $LASTEXITCODE" }
Get-ChildItem $Output -Filter *.msi | ForEach-Object { Write-Host "Built $($_.FullName)" }
