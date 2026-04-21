package LabirintTests;

import Model.GameField.Cell;
import Model.GameField.GameField;
import Model.GameField.GridRegion;
import Model.Labirint.Labirint;
import Model.Units.Wall;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LabirintTest {

    @Test
    void testGenerateSimpleDoesNotPlaceWallsOnEntranceAndExit() {
        GameField field = new GameField(20, 20);
        int left = 5, top = 5, width = 10, height = 10;
        GridRegion region = new GridRegion(field.getCell(top, left), width, height);
        Labirint lab = new Labirint(region);
        lab.generateSimple();

        Cell entrance = lab.getEntranceCell();
        Cell exit = lab.getExitCell();

        assertFalse(entrance.getUnit() instanceof Wall, "Entrance should not be a wall");
        assertFalse(exit.getUnit() instanceof Wall, "Exit should not be a wall");
    }

    @Test
    void testGenerateSimplePlacesWallsInsideRegion() {
        GameField field = new GameField(20, 20);
        int left = 5, top = 5, width = 10, height = 10;
        GridRegion region = new GridRegion(field.getCell(top, left), width, height);
        Labirint lab = new Labirint(region);
        lab.generateSimple();

        boolean hasWalls = false;
        for (Cell cell : region) {
            if (cell.getUnit() instanceof Wall) {
                hasWalls = true;
                break;
            }
        }
        assertTrue(hasWalls, "Region should contain at least one wall");
    }

    @Test
    void testRegionBoundsRespected() {
        GameField field = new GameField(20, 20);
        int left = 5, top = 5, width = 10, height = 10;
        GridRegion region = new GridRegion(field.getCell(top, left), width, height);
        Labirint lab = new Labirint(region);
        lab.generateSimple();

        for (Cell cell : region) {
            int row = cell.getRow();
            int col = cell.getCol();
            assertTrue(row >= top && row < top + height, "Row out of region");
            assertTrue(col >= left && col < left + width, "Col out of region");
        }
    }
}