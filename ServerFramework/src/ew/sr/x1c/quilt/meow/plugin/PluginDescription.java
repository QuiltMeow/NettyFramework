package ew.sr.x1c.quilt.meow.plugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PluginDescription {

    private String name;
    private String main;
    private String version;
    private String author;
    private Set<String> depend = new HashSet<>();
    private Set<String> softDepend = new HashSet<>();
    private File file;
    private String description;
}
