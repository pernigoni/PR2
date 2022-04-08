open Interprete_scoping_dinamico;;

(*  ocamlc interprete_scoping_dinamico.ml
    ocamlc interprete_scoping_dinamico.cmo test_interprete_scoping_dinamico.ml 
    ocaml

    #load "interprete_scoping_dinamico.cmo";;
    #use "test_interprete_scoping_dinamico.ml";;

    ^C

    del *.cmo, *.cmi, *.exe     *)

eval (Let ("x", EInt 1, 
        Let ("f", Fun ("y", Add (Den "x", Den "y")), 
            Let ("x", EInt 10, Apply (Den "f", EInt 3))))) empty_env;;
(* - : evT = Int 13 (scoping dinamico) *)

eval (Let ("x", EInt 1, 
        Let ("f", DFun ("y", Add (Den "x", Den "y"), [EInt 1; EInt 2; EInt 3]),
            Let ("x", EInt 10, Apply (Den "f", EInt 3))))) empty_env;;
(* - : evT = Int 13 *)

eval (Let ("x", EInt 1, 
        Let ("f", ParamsFun (["y"; "z"], Add (Den "x", Den "y")), 
            Let ("x", EInt 10, ApplyParams (Den "f", [EInt 3; EInt 1000]))))) empty_env;;
(* - : evT = Int 13 *)

eval (Let ("x", EInt 1, 
        Let ("f", Fun ("y", Add (Den "x", Den "y")), 
            Let ("x", EInt 10, Iterate (Den "f", EInt 3, EInt 10))))) empty_env;;
(* - : evT = Int 103 *)

let bodyfact =
    IfThenElse (Equals (Den "n", EInt 0),
        EInt 1,
        Times (Den "n", Apply (Den "fact", Sub (Den "n", EInt 1))));;

eval (Let ("fact", Fun ("n", bodyfact), Apply (Den "fact", EInt 7))) empty_env;;
(* - : evT = Int 5040 *)