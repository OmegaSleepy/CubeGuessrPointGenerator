package org.omega.core;

import de.pauleff.jmcx.formats.anvil.AnvilReader;

import java.io.File;
import java.io.IOException;

public class Main {
    public static final File world = new File("/home/martin/Documents/old world/s1/world/region");

    static void main () {
        try (AnvilReader reader = new AnvilReader(new File(world.getAbsoluteFile()+"/r.-1.4.mca"))){

            var region = reader.readRegion();
            var chunks = region.getChunks();

        } catch (IOException e){

        }
    }
}
