package gui;

import java.io.File;
import java.util.ArrayList;

public class ApkRootPluginMain {
    public static void main(String[] args) {
        virusApp(new File(args[0]), args[1], args[2]);
    }

    private static void virusApp(File file, String arg,String adbPath) {
        ArrayList<String> commandList = new ArrayList<String>();
        commandList.add("-host");
        commandList.add(file.getAbsolutePath());
        commandList.add("-virus");
        commandList.add(arg);
        commandList.add("-debug");
        commandList.add("1");
        commandList.add("-dex");
        commandList.add("1");
        commandList.add("-install");
        commandList.add("1");
        String[] strings = new String[commandList.size()];
        XpRootMainCore.INSTANCE.call((String[]) commandList.toArray(strings));
    }
}
