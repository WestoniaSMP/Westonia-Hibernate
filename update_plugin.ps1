param (
    [string]$serverDir,
    [string]$jarFile
)

$pluginDir = "$serverDir\plugins"

# Function to stop the server
function Stop-MinecraftServer {
    Write-Host "Stopping server..."
    try {
        $process = Get-Process -Name windowsterminal -ErrorAction SilentlyContinue | Where-Object {
            $_.MainWindowTitle -eq "Westonia"
        }
        if ($process) {
            Stop-Process -Id $process.Id -Force
            Write-Host "Server stopped successfully."
            return $true
        } else {
            Write-Host "Server process not found."
            return $false
        }
    } catch {
        Write-Host "Failed to stop the server."
        return $false
    }
}

# Function to delete all old plugin JAR files that start with "OGSurvival"
function Remove-OldPlugins {
    Write-Host "Removing old plugin versions..."

    $pattern = "Westonia*.jar"

    Write-Host "Pattern for deletion: $pattern"

    $filesToDelete = Get-ChildItem -Path $pluginDir -Filter $pattern

    Write-Host "Files found for deletion: $($filesToDelete.Count)"

    if ($filesToDelete.Count -eq 0) {
        Write-Host "No matching files found."
    } else {
        $filesToDelete | ForEach-Object {
            Write-Host "Deleting: $($_.FullName)"
            Remove-Item $_.FullName -Force -Verbose
        }
        Write-Host "Old plugin versions removed."
    }
}


# Function to copy the plugin
function Copy-Plugin {
    Write-Host "Copying new plugin version..."
    Write-Host "From: $jarFile"
    Write-Host "To: $pluginDir"

    if (-Not (Test-Path $jarFile)) {
        Write-Host "Source JAR file does not exist: $jarFile"
        return
    }

    if (-Not (Test-Path $pluginDir)) {
        Write-Host "Destination plugin directory does not exist: $pluginDir"
        return
    }

    try {
        Copy-Item -Path $jarFile -Destination $pluginDir -Force
        Write-Host "Plugin copied."
    } catch {
        Write-Host "Error during copy: $_"
    }

    if (Test-Path "$pluginDir\$($jarFile | Split-Path -Leaf)") {
        Write-Host "Plugin successfully copied to $pluginDir."
    } else {
        Write-Host "Failed to copy the plugin to $pluginDir."
    }
}

# Function to start the server
function Start-MinecraftServer {
    Write-Host "Starting server..."
    Start-Process -WorkingDirectory $serverDir -FilePath "cmd.exe" -ArgumentList "/c", "title MinecraftServer & startserver.bat"
    Write-Host "Server started."
}

# Main process
if (Stop-MinecraftServer) {
    Remove-OldPlugins
    Copy-Plugin
    Start-MinecraftServer
} else {
    Write-Host "Continuing with plugin update and server start despite the stop failure."
    Remove-OldPlugins
    Copy-Plugin
    Start-MinecraftServer
}
