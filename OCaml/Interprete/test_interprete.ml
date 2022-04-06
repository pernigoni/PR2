open Interprete;;

(*  ocamlc interprete.ml
    ocamlc interprete.cmo test_interprete.ml 
    ocaml
    
    #load "interprete.cmo";;
    #use "test_interprete.ml";;

    ^C

    del *.cmo, *.cmi, *.exe     *)

eval (Let ("x", EInt 1, 
        Let ("f", Fun ("y", Add (Den "x", Den "y")), 
            Let ("x", EInt 10, Apply (Den "f", EInt 3))))) empty_env;;
(* - : evT = Int 4 (scoping statico, altrimenti avrebbe restituito 13) *)

eval (Let ("x", EInt 1000, IfThenElse (IsZero (Den "x"), Den "x", Add (Den "x", EInt 1)))) empty_env;;
(* - : evT = Int 1001 *)

(* RECORD *)

eval (Select (ERecord [(Lab "matricola", EInt 597958); (Lab "anno", EInt 2000); (Lab "s", EInt 1)], Lab "anno")) empty_env;;
(* - : evT = Int 2000 *)

(* FUNZIONI NON RICORSIVE *)

eval (Let ("myfun", Fun ("x", Add (Den "x", EInt 1000)), Apply (Den "myfun", EInt 77))) empty_env;;
(* - : evT = Int 1077 *)

let square = Fun ("x", Times (Den "x", Den "x"));;

let cube = Fun ("x", Times (Den "x", Times (Den "x", Den "x")));;

(* FUNZIONI COME RISULTATO *)

eval (Let ("square", square,
        Let ("cube", cube,
            Let ("myfun_retFun", Fun ("bool", IfThenElse (Den "bool", Den "square", Den "cube")), Apply (Den "myfun_retFun", EFalse))))) empty_env;;
(* - : evT = Closure ("x", Times (Den "x", Times (Den "x", Den "x")), [("square", Closure ("x", Times (Den "x", Den "x"), [("", Unbound)])); ("", Unbound)]) *)

(* FUNZIONI COME PARAMETRO *)

eval (Let ("square", square,
        Let ("cube", cube,
            Let ("myfun_paramFun", Fun ("f", Apply (Den "f", EInt 3)), Apply (Den "myfun_paramFun", Den "cube"))))) empty_env;;
(* - : evT = Int 27 *)

(* FUNZIONI RICORSIVE *)

let bodyfact =
    IfThenElse (Equals (Den "n", EInt 0), 
        EInt 1, 
        Times (Den "n", Apply (Den "fact", Sub (Den "n", EInt 1))));;

eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 0))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 1))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 2))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 3))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 4))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 5))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 6))) empty_env;;
eval (LetRec ("fact", "n", bodyfact, Apply (Den "fact", EInt 7))) empty_env;;
(* 1 1 2 6 24 120 720 5040 *)

(*  int fib(int n)
    {
        if(n < 0) 
            return -1;
        else if(n == 0) 
            return 0;
        else if(n == 1) 
            return 1;
        else 
            return fib(n - 1) + fibonacci(n - 2);
    } 
*)
let bodyfib =
    IfThenElse (LessThan (Den "n", EInt 0), 
        EInt (-1), 
        IfThenElse (Equals (Den "n", EInt 0), 
            EInt 0,
            IfThenElse (Equals (Den "n", EInt 1), 
                EInt 1,
                Add (Apply (Den "fib", Sub (Den "n", EInt 1)), Apply (Den "fib", Sub (Den "n", EInt 2))))));;

eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 0))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 1))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 2))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 3))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 4))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 5))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 6))) empty_env;;
eval (LetRec ("fib", "n", bodyfib, Apply (Den "fib", EInt 7))) empty_env;;
(* 0 1 1 2 3 5 8 13 *)

(* FUNZIONI A DOMINIO FINITO *)

eval (Let ("g", DFun ("x", Add (Den "x", EInt 1000), [EInt 1; EInt 2]), Apply (Den "g", EInt 2))) empty_env;;
(* - : evT = Int 1002 *)

eval (Let ("h", DFun ("x", Add (Den "x", EInt 1000), [EInt 1; EInt 3]), Apply (Den "h", EInt 2))) empty_env;;
(* - : evT = Unbound *)

(* FUNZIONI A PIÙ VARIABILI *)

eval (Let ("fparams", ParamsFun (["x"; "y"; "z"], Add (Den "x", Add (Den "y", Add (Den "z", EInt 1000)))), ApplyParams (Den "fparams", [EInt 1; EInt 2; EInt 3]))) empty_env;;
(* - : evT = Int 1006 *)

(* ITERATE *)

eval (Let ("myfun", Fun ("x", Add (Den "x", EInt 1000)), Iterate (Den "myfun", EInt 7, EInt 3))) empty_env;;
(* - : evT = Int 3007 *)

(* STACK *)

let s = EStack (Val (EInt 3, Val (EInt 1, Val (EInt 2, Empty))));;
eval s empty_env;;
(* - : evT = Stack [Int 3; Int 1; Int 2] *)

eval (Push (s, EInt 4)) empty_env;; 
(* - : evT = Stack [Int 4; Int 3; Int 1; Int 2] *)

eval (Pop (s)) empty_env;;
(* - : evT = Stack [Int 1; Int 2] *)