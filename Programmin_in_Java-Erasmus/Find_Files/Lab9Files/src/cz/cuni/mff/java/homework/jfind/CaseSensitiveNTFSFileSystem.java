package cz.cuni.mff.java.homework.jfind;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CaseSensitiveNTFSFileSystem
        extends FileSystem
{
    private static final Pattern MYSYNTAX = Pattern.compile("glob:\\*(\\..*)");

    private final FileSystem fs;

    // "fs" is the "genuine" FileSystem provided by the JVM
    public CaseSensitiveNTFSFileSystem(final FileSystem fs)
    {
        this.fs = fs;
    }

    @Override
    public FileSystemProvider provider() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public String getSeparator() {
        return null;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return null;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return null;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return null;
    }

    @Override
    public Path getPath(String first, String... more) {
        return null;
    }

    @Override
    public PathMatcher getPathMatcher(final String syntaxAndPattern)
    {
        final Matcher matcher = MYSYNTAX.matcher(syntaxAndPattern);
        if (!matcher.matches())
            throw new UnsupportedOperationException();
        final String suffix = matcher.group(1);
        final PathMatcher orig = fs.getPathMatcher(syntaxAndPattern);

        return new PathMatcher()
        {
            @Override
            public boolean matches(final Path path)
            {
                return orig.matches(path)
                        && path.getFileName().endsWith(suffix);
            }
        };
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return null;
    }

    @Override
    public WatchService newWatchService() throws IOException {
        return null;
    }

    // Delegate all other methods of FileSystem to "fs"
}
