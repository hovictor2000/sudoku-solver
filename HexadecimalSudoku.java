package edu.ics211.h09;

import java.util.ArrayList;

/**
 * Class for recursively finding a solution to a Hexadecimal Sudoku problem.
 *
 * @author Biagioni, Edoardo, Cam Moore
 *     date August 5, 2016
 *     missing solveSudoku, to be implemented by the students in ICS 211
 */
public class HexadecimalSudoku {

  /**
   * Find an assignment of values to sudoku cells that makes the sudoku valid.
   *
   * @param sudoku the sudoku to be solved.
   * @return whether a solution was found if a solution was found, the sudoku is
   *         filled in with the solution if no solution was found, restores the
   *         sudoku to its original value.
   */
  public static boolean solveSudoku(int[][] sudoku) {
    // For each cell on in the sudoku
    for (int i = 0; i < sudoku.length; i++) {
      for (int j = 0; j < sudoku[i].length; j++) {
        if (sudoku[i][j] == -1) {
          ArrayList<Integer> values = legalValues(sudoku, i, j);
          if (values == null) {
            return false;
          }
          // For each possible value, try to solve it
          for (int value : values) {
            sudoku[i][j] = value;
            // Check if for this value, it works with  other possible solutions
            if (solveSudoku(sudoku)) {
              return true;
            }
          }
          sudoku[i][j] = -1;
          return false;
        }
      }
    }
    return checkSudoku(sudoku, false);
  }

  /**
   * Find the legal values for the given sudoku and cell.
   * Newer code was inspired from lecture on 10/28
   *
   * @param sudoku the sudoku being solved.
   * @param row the row of the cell to get values for.
   * @param column the column of the cell.
   * @return an ArrayList of the valid values.
   */
  public static ArrayList<Integer> legalValues(int[][] sudoku, int row, int column) {
    ArrayList<Integer> values = new ArrayList<Integer>();
    // Old code, much more inefficient
    //    if (sudoku[row][column] != -1) {
    //      return null;
    //    }
    //    // Check each possible value for conflicts
    //    for (int i = 0; i < 16; i++) {
    //      sudoku[row][column] = i;
    //      if (checkSudoku(sudoku, false)) {
    //        values.add(i);
    //      } else {
    //        sudoku[row][column] = -1;
    //      }
    //    }
    
    // Loops to add any possible value.
    for (int i = 0; i < 16; i ++) {
      values.add(i);
    }
    // Loops to check entire row for duplicate value.
    for (int j = 0; j < 16; j++) {
      if (values.contains(sudoku[row][j])) { 
        values.remove(Integer.valueOf(sudoku[row][j]));
      }
    }
    // Loops to check entire column for duplicate value.
    for (int i = 0; i < 16; i++) {
      if (values.contains(sudoku[i][column])) { 
        values.remove(Integer.valueOf(sudoku[i][column]));
      }
    }
    // Loops to check 4x4 for duplicate value.
    // Loop taken from checkSudoku method written  by Biagioni, Edoardo, and Cam Moore.
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        // Converts i and j into index of 4x4 that contains given row and column.
        int testRow = (row / 4 * 4) + i;
        int testCol = (column / 4 * 4) + j;
        if (values.contains(sudoku[testRow][testCol])) {
          values.remove(Integer.valueOf(sudoku[testRow][testCol]));
        }
      }
    }
    if (values.size() == 0) {
      return null;
    }
    return values;
  }


  /**
   * checks that the sudoku rules hold in this sudoku puzzle. cells that contain
   * 0 are not checked.
   *
   * @param sudoku the sudoku to be checked.
   * @param printErrors whether to print the error found, if any.
   * @return true if this sudoku obeys all of the sudoku rules, otherwise false.
   */
  public static boolean checkSudoku(int[][] sudoku, boolean printErrors) {
    if (sudoku.length != 16) {
      if (printErrors) {
        System.out.println("sudoku has " + sudoku.length + " rows, should have 16");
      }
      return false;
    }
    for (int i = 0; i < sudoku.length; i++) {
      if (sudoku[i].length != 16) {
        if (printErrors) {
          System.out.println("sudoku row " + i + " has "
              + sudoku[i].length + " cells, should have 16");
        }
        return false;
      }
    }
    /* check each cell for conflicts */
    for (int i = 0; i < sudoku.length; i++) {
      for (int j = 0; j < sudoku.length; j++) {
        int cell = sudoku[i][j];
        if (cell == -1) {
          continue; /* blanks are always OK */
        }
        if ((cell < 0) || (cell > 16)) {
          if (printErrors) {
            System.out.println("sudoku row " + i + " column " + j
                + " has illegal value " + String.format("%02X", cell));
          }
          return false;
        }
        /* does it match any other value in the same row? */
        for (int m = 0; m < sudoku.length; m++) {
          if ((j != m) && (cell == sudoku[i][m])) {
            if (printErrors) {
              System.out.println("sudoku row " + i + " has " + String.format("%X", cell)
                  + " at both positions " + j + " and " + m);
            }
            return false;
          }
        }
        /* does it match any other value it in the same column? */
        for (int k = 0; k < sudoku.length; k++) {
          if ((i != k) && (cell == sudoku[k][j])) {
            if (printErrors) {
              System.out.println("sudoku column " + j + " has " + String.format("%X", cell)
                  + " at both positions " + i + " and " + k);
            }
            return false;
          }
        }
        /* does it match any other value in the 4x4? */
        for (int k = 0; k < 4; k++) {
          for (int m = 0; m < 4; m++) {
            int testRow = (i / 4 * 4) + k; /* test this row */
            int testCol = (j / 4 * 4) + m; /* test this col */
            if ((i != testRow) && (j != testCol) && (cell == sudoku[testRow][testCol])) {
              if (printErrors) {
                System.out.println("sudoku character " + String.format("%X", cell) + " at row "
                    + i + ", column " + j + " matches character at row " + testRow + ", column "
                    + testCol);
              }
              return false;
            }
          }
        }
      }
    }
    return true;
  }


  /**
   * Converts the sudoku to a printable string.
   *
   * @param sudoku the sudoku to be converted.
   * @param debug whether to check for errors.
   * @return the printable version of the sudoku.
   */
  public static String toString(int[][] sudoku, boolean debug) {
    if ((!debug) || (checkSudoku(sudoku, true))) {
      String result = "";
      for (int i = 0; i < sudoku.length; i++) {
        if (i % 4 == 0) {
          result = result + "+---------+---------+---------+---------+\n";
        }
        for (int j = 0; j < sudoku.length; j++) {
          if (j % 4 == 0) {
            result = result + "| ";
          }
          if (sudoku[i][j] == -1) {
            result = result + "  ";
          } else {
            result = result + String.format("%X", sudoku[i][j]) + " ";
          }
        }
        result = result + "|\n";
      }
      result = result + "+---------+---------+---------+---------+\n";
      return result;
    }
    return "illegal sudoku";
  }
}
