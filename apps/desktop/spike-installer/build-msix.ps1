# Spike S-07: packs the Compose app image into an unsigned MSIX (manual desktop-to-UWP conversion).
param(
    [Parameter(Mandatory)][string]$AppImage,
    [Parameter(Mandatory)][string]$SpikeDir,
    [Parameter(Mandatory)][string]$Output,
    [Parameter(Mandatory)][string]$Version
)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing

$makeappx = Get-ChildItem "${env:ProgramFiles(x86)}\Windows Kits\10\bin\*\x64\makeappx.exe" |
    Sort-Object FullName -Descending | Select-Object -First 1
if (-not $makeappx) { throw 'makeappx.exe not found (Windows SDK is required)' }

$layout = Join-Path $Output 'msix-layout'
Remove-Item $layout -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force (Join-Path $layout 'Assets') | Out-Null
Copy-Item $AppImage (Join-Path $layout 'app') -Recurse

(Get-Content (Join-Path $SpikeDir 'msix\AppxManifest.xml') -Raw).Replace('@VERSION@', $Version) |
    Set-Content (Join-Path $layout 'AppxManifest.xml') -Encoding UTF8

# Placeholder logos; the real icon set comes with step 2.2. Unscaled file names, so no resources.pri is needed.
foreach ($size in 44, 150) {
    $bitmap = New-Object System.Drawing.Bitmap $size, $size
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $graphics.Clear([System.Drawing.Color]::FromArgb(0xF2, 0xB8, 0x4B))
    $graphics.Dispose()
    $bitmap.Save((Join-Path $layout "Assets\Square${size}x${size}Logo.png"), [System.Drawing.Imaging.ImageFormat]::Png)
    $bitmap.Dispose()
}

$package = Join-Path $Output 'Pixroost.msix'
Remove-Item $package -Force -ErrorAction SilentlyContinue
& $makeappx.FullName pack /d $layout /p $package /o
if ($LASTEXITCODE -ne 0) { throw "makeappx failed with exit code $LASTEXITCODE" }
Write-Host "Built $package"
