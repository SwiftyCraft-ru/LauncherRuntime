package pro.gravit.launcher.client;

import pro.gravit.launcher.LauncherEngine;
import pro.gravit.launcher.client.events.ClientPreGuiPhase;
import pro.gravit.launcher.client.gui.raw.MessageManager;
import pro.gravit.launcher.gui.RuntimeProvider;
import pro.gravit.launcher.modules.LauncherInitContext;
import pro.gravit.launcher.modules.LauncherModule;
import pro.gravit.launcher.modules.LauncherModuleInfo;
import pro.gravit.utils.Version;
import pro.gravit.utils.helper.JVMHelper;

import javax.swing.*;

public class JavaRuntimeModule extends LauncherModule {
    public RuntimeProvider provider;

    public JavaRuntimeModule() {
        super(new LauncherModuleInfo("StdJavaRuntime", new Version(1, 0, 0),
                0, new String[]{}, new String[]{"runtime"}));
    }

    public static void noJavaFxAlert() {
        String message = String.format("Библиотеки JavaFX не найдены. У вас %s(x%d) ОС %s(x%d). Java %s. Установите Java с поддержкой JavaFX, например OracleJRE 8 x%d с официального сайта.\nЕсли вы не можете решить проблему самостоятельно обратитесь к администрации своего проекта", JVMHelper.RUNTIME_MXBEAN.getVmName(),
                JVMHelper.JVM_BITS, JVMHelper.OS_TYPE.name, JVMHelper.OS_BITS, JVMHelper.RUNTIME_MXBEAN.getSpecVersion(), JVMHelper.OS_BITS);
        JOptionPane.showMessageDialog(null, message, "GravitLauncher", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void init(LauncherInitContext initContext) {
        registerEvent(this::preGuiPhase, ClientPreGuiPhase.class);
    }

    public void preGuiPhase(ClientPreGuiPhase phase) {
        try {
            Class.forName("javafx.application.Application");
        } catch (ClassNotFoundException e) {
            noJavaFxAlert();
            LauncherEngine.exitLauncher(0);
        }
        provider = new StdJavaRuntimeProvider();
        phase.runtimeProvider = provider;
    }
}
