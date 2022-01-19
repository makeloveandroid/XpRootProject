package action.utils;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class OSUtil {
    @Nullable
    public static String findBestExecutable(@NotNull String commandName) {
        String[] pathElements = StringUtils.split(System.getenv("PATH"), File.pathSeparatorChar);
        if (pathElements == null) {
            return null;
        }

        return findBestExecutable(commandName, Arrays.asList(pathElements));
    }

    @Nullable
    public static String findBestExecutable(@NotNull String commandName, @NotNull List<String> paths) {
        for (String path : paths) {
            String executablePath = findExecutable(commandName, path);
            if (executablePath != null) {
                return executablePath;
            }
        }

        return null;
    }

    @Nullable
    public static String findExecutable(@NotNull String commandName, String path) {
        String fullPath = path + File.separatorChar + commandName;
        File command = new File(fullPath);
        return command.exists() ? command.getAbsolutePath() : null;
    }

    public static String findExecuteFilePath(String rootPath, String executeName) {
        if (rootPath == null) {
            return null;
        }
        if (rootPath.endsWith(executeName)) {
            return rootPath;
        }
        File file = new File(rootPath);
        if (!file.exists()) {
            return null;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                return findExecuteFilePath(f.getPath(), executeName);
            } else {
                if (f.getPath().endsWith(executeName)) {
                    return f.getPath();
                }
            }
        }
        return null;
    }
}
