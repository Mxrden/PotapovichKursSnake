package Model.Labirint;

import Model.GameField.GridRegion;

public interface LabirintGenerator {
    Labirint generate(GridRegion region);
}