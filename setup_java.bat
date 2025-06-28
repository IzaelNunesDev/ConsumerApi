@echo off
echo Configurando JAVA_HOME para o projeto RotaFácil...

REM Verificar se o Android Studio está instalado
if exist "C:\Program Files\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    echo JAVA_HOME configurado para: %JAVA_HOME%
) else if exist "C:\Program Files (x86)\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files (x86)\Android\Android Studio\jbr"
    echo JAVA_HOME configurado para: %JAVA_HOME%
) else (
    echo Android Studio não encontrado. Verifique se está instalado.
    echo Você pode configurar manualmente o JAVA_HOME apontando para uma instalação do JDK.
    pause
    exit /b 1
)

REM Adicionar ao PATH
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo.
echo Configuração concluída!
echo JAVA_HOME: %JAVA_HOME%
echo.
echo Agora você pode executar: gradlew assembleDebug
echo.
pause 