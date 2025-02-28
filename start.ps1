# Ruta de la carpeta a eliminar
$folderPath = "C:\Users\VALENT~1\AppData\Local\Temp\kafka-streams"

# Parece ser que es problema de la high level api, ya que el error ocurrer con el AGR y a veces con el MIXBI
# Eliminar la carpeta si existe
if (Test-Path $folderPath) {
    Remove-Item -Recurse -Force $folderPath
    Write-Host "Carpeta eliminada: $folderPath"
} else {
    Write-Host "La carpeta no existe, no se requiere eliminación."
}

# Inicializar los contenedores
docker compose up -d

# Ejecutar Python app
python python\dashboard.py