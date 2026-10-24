# Spike S-07: builds Pixroost-<version>-setup.exe with Inno Setup from the Compose app image.
param(
    [Parameter(Mandatory)][string]$AppImage,
    [Parameter(Mandatory)][string]$SpikeDir,
    [Parameter(Mandatory)][string]$Output,
    [Parameter(Mandatory)][string]$Version
)
$ErrorActionPreference = 'Stop'

function Find-Iscc {
    $candidates = @("${env:ProgramFiles(x86)}\Inno Setup 6\ISCC.exe", "$env:ProgramFiles\Inno Setup 6\ISCC.exe")
    $candidates | Where-Object { Test-Path $_ } | Select-Object -First 1
}

$iscc = Find-Iscc
if (-not $iscc) {
    Write-Host 'Inno Setup not found, installing with choco...'
    choco install innosetup -y --no-progress
    $iscc = Find-Iscc
}
if (-not $iscc) { throw 'ISCC.exe not found' }

New-Item -ItemType Directory -Force $Output | Out-Null
& $iscc "/DAppImage=$AppImage" "/DOutputDir=$Output" "/DVersion=$Version" (Join-Path $SpikeDir 'pixroost.iss')
if ($LASTEXITCODE -ne 0) { throw "ISCC failed with exit code $LASTEXITCODE" }
Get-ChildItem $Output -Filter *setup.exe | ForEach-Object { Write-Host "Built $($_.FullName)" }
