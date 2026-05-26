# CubeGuessrPointGenerator

A Java program that generates valid gameplay points from Minecraft worlds for the **WhereTheChunkAmI** geoguessr-style game. This tool efficiently extracts landmark locations without loading the full world or Minecraft itself.

## Overview

CubeGuessrPointGenerator is the data layer backbone for [WhereTheChunkAmI](https://github.com/Neptune4918/WhereTheChunkAmI-frontend), a geoguessr-style game centered around Minecraft. This project generates the points that players must identify and the panoramic views they'll use to guess locations.

### Key Features

- **Efficient Point Generation**: Produces large volumes of valid points without triggering garbage collection
- **Region-Based Loading**: Loads only necessary region files (`.mca`) to minimize memory footprint
- **Raw File Parsing**: Reads Minecraft world data directly without loading Minecraft itself
- **Flexible Configuration**: Define regions of interest using either circular areas or custom polygons
- **Customizable Algorithms**: Implement your own point selection strategy via the `IPointAlgorithm` interface

### How It Works

Points are intelligently filtered to ensure quality:
- ❌ Excludes points in the air
- ❌ Excludes points in oceans
- ❌ Excludes points underground
- ✅ Returns only valid, explorable surface locations

## Technology Stack

- **Language**: Java 25
- **Build Tool**: Maven
- **Core Dependency**: [pauleff's jmcx](https://github.com/pauleff/jmcx) (with custom abstraction layer)

## Getting Started

### Prerequisites

- Java 25+
- Maven 3.6+

### Building

```bash
mvn clean package
```

### Configuration

Configuration is handled in `org.omega.core.Main`'s `main` method. Here's what you can customize:

#### 1. **Set Your World**

Modify the world file path at the top of the `Main` class definition:

```java
private static final File WORLD_PATH = new File("/path/to/your/minecraft/world");
```

#### 2. **Define Regions of Interest**

Use either a circular region or a custom polygon:

**Circle (Center + Radius):**
```java
CircleCenter region = new CircleCenter(
    new PointXYZ(x, y, z),  // Center coordinates
    radius                   // Radius in blocks
);
```

**Polygon (Multiple Points):**
```java
Polygon region = new Polygon(Arrays.asList(
    new PointXYZ(x1, y1, z1),
    new PointXYZ(x2, y2, z2),
    new PointXYZ(x3, y3, z3)
    // ... more points
));
```

#### 3. **Generate Points**

Use the default algorithm or implement your own:

```java
// Default algorithm
int numPoints = 1000;
List<PointXYZ> points = genPoints(numPoints);

// Custom algorithm
class MyPointAlgorithm implements IPointAlgorithm {
    @Override
    public List<PointXYZ> generate(int count) {
        // Your logic here
        return pointsList;
    }
}

List<PointXYZ> customPoints = new MyPointAlgorithm().generate(numPoints);
```

## Architecture

The project uses a custom abstraction layer on top of `jmcx` to:
- Minimize world data in memory
- Efficiently filter terrain characteristics
- Support multiple region definitions
- Enable algorithm extensibility

## Performance

CubeGuessrPointGenerator is highly optimized:
- Generates thousands of points without triggering garbage collection
- Only loads region files being actively processed
- Direct `.mca` file parsing avoids Minecraft engine overhead

## Project Context

This is part of the **WhereTheChunkAmI** project ecosystem:
- **Frontend**: [WhereTheChunkAmI-frontend](https://github.com/Neptune4918/WhereTheChunkAmI-frontend) by Neptune4918
- **Purpose**: Create a geoguessr-style guessing game using Minecraft world data

## License

[Specify your license here]

## Contributing

Contributions welcome! Feel free to:
- Optimize point generation algorithms
- Add new region definition types
- Improve terrain filtering logic

## Author

OmegaSleepy
