# Spike S-07, runs inside Windows Sandbox: installs the .msi silently (or the Inno Setup .exe with -Exe),
# prints the firewall rule and starts the app.
param([switch]$Exe)
$ErrorActionPreference = 'Continue'
$log = 'C:\Users\Public\pixroost-install.log'

if ($Exe) {
    $setup = Get-ChildItem C:\Test -Filter 'Pixroost*setup.exe' | Select-Object -First 1
    if (-not $setup) { Write-Host 'No Pixroost*setup.exe in C:\Test'; return }
    Write-Host "Installing $($setup.Name) silently..."
    $install = Start-Process $setup.FullName -ArgumentList "/VERYSILENT /SUPPRESSMSGBOXES /NORESTART /TASKS=desktopicon /LOG=$log" -Wait -PassThru
    Write-Host "setup exit code: $($install.ExitCode) (0 = ok, log: $log)"
    $uninstall = "msiexec-not-used: run unins000.exe /VERYSILENT in the install folder"
} else {
    $msi = Get-ChildItem C:\Test -Filter 'Pixroost*.msi' | Select-Object -First 1
    if (-not $msi) { Write-Host 'No Pixroost*.msi in C:\Test'; return }
    Write-Host "Installing $($msi.Name) silently..."
    $install = Start-Process msiexec.exe -ArgumentList "/i `"$($msi.FullName)`" /qn /l*v $log" -Wait -PassThru
    Write-Host "msiexec exit code: $($install.ExitCode) (0 = ok, log: $log)"
    $uninstall = "msiexec /x `"$($msi.FullName)`" /qn"
}

Write-Host "`n--- Get-NetFirewallRule -DisplayName Pixroost* ---"
Get-NetFirewallRule -DisplayName 'Pixroost*' | Format-Table DisplayName, Enabled, Direction, Action, Profile -AutoSize
Get-NetFirewallRule -DisplayName 'Pixroost*' | Get-NetFirewallApplicationFilter | Format-Table Program -AutoSize

$exePath = Join-Path $env:ProgramFiles 'Pixroost\Pixroost.exe'
Write-Host "Launching $exePath"
if (Test-Path $exePath) { Start-Process $exePath } else { Write-Host 'Pixroost.exe not found' }

Write-Host "`nTo test uninstall and rule removal, run:"
Write-Host "  $uninstall ; Get-NetFirewallRule -DisplayName 'Pixroost*'"
