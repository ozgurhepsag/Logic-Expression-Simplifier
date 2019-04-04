# Logic Expression Simplifier using Quine McCluskey Algorithm

In this project, a simplification was designed tool for logic expressions. A windows application was developed to design this tool by using Java. The main purpose is to simplify given expressions which are either in Boolean format (Sum of Products) or as truth table. The functions will be 1 output and up to 4 inputs. The tool should have the following specifications:

● Reading the files that include expressions. File types will be given as .tt (for truth table - see example 1) or .be(Boolean expression - see example 2)

● Accepting logic expression inputs from GUI as a truth table or an equation.

● Exporting simplified expression as files (.tt or .be).

● For the simplification two methods should be used. One of them is Boolean Algebra in which, used steps should be shown. The other is Karnaugh Map in which groups that used for simplification should be shown.

● Showing any function as a truth table, equation or Karnaugh map.

● Displaying an alternative simplified functions for each if they exist.

● Showing corresponding expression term on the Karnaugh Map as highlighted and vice versa.

● JavaFX library will be used for the project.

Truth Table Format in .tt files: <br />
>A,B,C;F <br />
>0,0,0;0 <br />
>0,0,1;1 <br />
>… <br />
>1,1,1;0 <br />

Example 2 - Logic Expression Format: <br />
>F = A’.B’.C + A’.B.C’ + A.B’.C + A.B’.C’
