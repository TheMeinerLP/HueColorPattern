package net.onelitefeather.rockbrush.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;

import java.util.Random;

public final class DiamonSquareBrush implements Brush {

    private final Double strong;

    private final Random random = new Random();

    public DiamonSquareBrush(Double strong) {
        this.strong = strong;
    }

    @Override
    public void build(EditSession editSession, BlockVector3 position, Pattern pattern, double size) throws MaxChangedBlocksException {
        int mapSize = (int) size;
        int halfSize = (int) (size / 2);
        double[][] heightMap = new double[mapSize][mapSize];
        for (int x = 0; x < mapSize; x++) {
            for (int z = 0; z < mapSize; z++) {
                for (int y = 0; y < (int) heightMap[x][z]; y++) {
                    double xOffset = x - halfSize;
                    double zOffset = z - halfSize;
                    heightMap[x][z] = position.add((int) xOffset, (int) heightMap[x][z], (int) zOffset).getY();
                }
            }
        }
        /*heightMap[0][0] = random.nextDouble();
        heightMap[0][mapSize - 1] = random.nextDouble();
        heightMap[mapSize - 1][0] = random.nextDouble();
        heightMap[mapSize - 1][mapSize - 1] = random.nextDouble();*/

        diamondSquare(heightMap, 0, 0, mapSize - 1, mapSize - 1, strong);

       /* for (int x = 0; x < mapSize; x++) {
            for (int z = 0; z < mapSize; z++) {
                heightMap[x][z] = position.add(x, (int) heightMap[x][z], z).getY();
            }
        }*/

        int smoothingIterations = 3; // Anzahl der Glättungsschritte
        double smoothingFactor = 0.5; // Stärke der Glättung

        for (int i = 0; i < smoothingIterations; i++) {
            heightMap = applyGaussianSmoothing(heightMap, smoothingFactor);
        }


        for (int x = 0; x < mapSize; x++) {
            for (int z = 0; z < mapSize; z++) {
                for (int y = 0; y < (int) heightMap[x][z]; y++) {
                    double xOffset = x - halfSize;
                    double yOffset = z - halfSize;
                    editSession.setBlock(position.add((int) xOffset, (y/* - position.getY()*/), (int) yOffset), pattern);
                }
            }
        }
    }

    private void diamondSquare(double[][] map, int x1, int y1, int x2, int y2, double range) {
        if (x2 - x1 <= 1 && y2 - y1 <= 1) {
            return;
        }

        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;

        // Diamond step
        double avg = (map[x1][y1] + map[x1][y2] + map[x2][y1] + map[x2][y2]) / 4;
        map[centerX][centerY] = avg + (randRange(-range, range));

        // Square step
        map[x1][centerY] = (map[x1][y1] + map[x1][y2] + map[centerX][centerY]) / 3 + (randRange(-range, range));
        map[x2][centerY] = (map[x2][y1] + map[x2][y2] + map[centerX][centerY]) / 3 + (randRange(-range, range));
        map[centerX][y1] = (map[x1][y1] + map[x2][y1] + map[centerX][centerY]) / 3 + (randRange(-range, range));
        map[centerX][y2] = (map[x1][y2] + map[x2][y2] + map[centerX][centerY]) / 3 + (randRange(-range, range));

        // Recurse on sub-quadrants
        diamondSquare(map, x1, y1, centerX, centerY, range / 2);
        diamondSquare(map, centerX, y1, x2, centerY, range / 2);
        diamondSquare(map, x1, centerY, centerX, y2, range / 2);
        diamondSquare(map, centerX, centerY, x2, y2, range / 2);
    }

    private double randRange(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    private double[][] applyGaussianSmoothing(double[][] map, double smoothingFactor) {
        int width = map.length;
        int height = map[0].length;
        double[][] smoothedMap = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                smoothedMap[x][y] = applyGaussianKernel(map, x, y, smoothingFactor);
            }
        }

        return smoothedMap;
    }

    private double applyGaussianKernel(double[][] map, int x, int y, double smoothingFactor) {
        int kernelSize = 5; // Größe des Filters
        int halfKernelSize = kernelSize / 2;

        double sum = 0.0;
        double weightSum = 0.0;

        for (int i = -halfKernelSize; i <= halfKernelSize; i++) {
            for (int j = -halfKernelSize; j <= halfKernelSize; j++) {
                int newX = x + i;
                int newY = y + j;

                if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length) {
                    double weight = calculateGaussianWeight(i, j, smoothingFactor);
                    sum += map[newX][newY] * weight;
                    weightSum += weight;
                }
            }
        }

        return sum / weightSum;
    }

    private double calculateGaussianWeight(int x, int y, double smoothingFactor) {
        // Implement Gaussian weight calculation here
        // Example: Gaussian function = e^(-(x^2 + y^2) / (2 * sigma^2))
        return Math.exp(-(x * x + y * y) / (2 * smoothingFactor * smoothingFactor));
    }
}
