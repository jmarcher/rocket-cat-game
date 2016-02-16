package uy.com.marcher.superjumper;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import uy.com.marcher.superjumper.Util.ActionResolver;

public class IOSLauncher extends IOSApplication.Delegate implements ActionResolver {
    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new SuperJumper(this), config);
    }

    @Override
    public void showOrLoadInterstital() {
        System.out.println("Nada");
    }
}