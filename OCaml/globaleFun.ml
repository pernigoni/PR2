(*  27/11/2020 
    Interprete per un linguaggio didattico funzionale in cui le funzioni sono dichiarate globalmente
    e non possono avere dichiarazioni locali di altre funzioni.
    Il programma ha la forma P = [fun_dcl list] main_exp.   *)

type ide = string;;

type exp =
    | EInt of int
    | Den of ide
    | App of ide * exp
    | Add of exp * exp
    | Sub of exp * exp
    | Mul of exp * exp
    | Ifz of exp * exp * exp
    | Eq of exp * exp
    | Leq of exp * exp
    | Not of exp
    | And of exp * exp
    | Or of exp * exp;;

type def = Fun of ide * ide * exp;; (* fun_name * param * body *)
(* Non serve un costrutto specifico per le funzioni ricorsive. *) 

type prog = Prog of (def list) * exp;; (* assomiglia al C *)

(*  Due possibilità per l'ambiente: 
        1) un solo ambiente, che contiene sia funzioni globali che identificatori associati a interi 
        (per semplicità non abbiamo booleani), introdotti per passaggio di parametri;
        2) due ambienti separati, uno globale per funzioni, uno locale per identificatori.
    Scegliamo la seconda opzione.   
    
    Nell'ambiente globale associo al nome di una funzione semplicemente una coppia: ide * exp. 
    L'ambiente globale è gestito come se fosse scoping dinamico, quindi passato all'invocazione, 
    per garantire che contenga tutte le funzioni dichiarate.    *)

type 't env = ide -> 't;; (* ambiente polimorfo *)

let bind (r : 't env) (i : ide) (v : 't) = (* estensione di ambiente *)
    function x -> if x = i then v else r x;;

exception EmptyEnv;;
exception WrongFun;;

let env0 = fun x -> raise EmptyEnv;; (* ambiente di default *)

(*  venv = int env
    denv = (ide * exp) env  *) 

(* valutazione di espressioni *)
let rec eval e venv denv = 
    match e with
    | EInt i -> i
    | Den s -> venv s
    | App (s, e1) -> 
        (match (denv s) with
        | (par, body) ->
            let v = (eval e1 venv denv) in
                let venv1 = (bind env0 par v) in (* SCOPING STATICO )
                let venv1 = (bind venv par v) in (* SCOPING DINAMICO *) *)
                    eval body venv1 denv
        | _ -> raise WrongFun)                                   
    | Add (e1, e2) -> (eval e1 venv denv) + (eval e2 venv denv)
    | Sub (e1, e2) -> (eval e1 venv denv) - (eval e2 venv denv)
    | Mul (e1, e2) -> (eval e1 venv denv) * (eval e2 venv denv)
    | Not e1 -> if ((eval e1 venv denv) = 0) then 1 else 0 (* booleani rappresentati da 0 e 1 *)
    | Or (e1, e2) -> if (eval e1 venv denv) = 0 then (eval e2 venv denv) else 1 
    | And (e1, e2) -> if (eval e1 venv denv) != 0 then (eval e2 venv denv) else 0
    | Eq (e1, e2) -> if ((eval e1 venv denv) = (eval e2 venv denv)) then 1 else 0 
    | Leq (e1, e2) -> if ((eval e1 venv denv) <= (eval e2 venv denv)) then 1 else 0
    | Ifz (e1, e2, e3) -> if (eval e1 venv denv) = 1 then (eval e2 venv denv) else (eval e3 venv denv);;

(* valutazione di dichiarazione: restituisce un ambiente globale *)
let rec dval (decls : def list) = 
    match decls with
    | [] -> env0
    | Fun (fname, par, body)::rest -> bind (dval rest) fname (par, body);;

(* valutazione di programma: valuta l'espressione usando l'ambiente globale ottenuto dalle dichiarazioni *)
let pval (p : prog) = 
    match p with
    | Prog (decls, exp) -> let fenv = (dval decls) in eval exp env0 fenv;;

(* TEST *)

let p1 = Prog ([], Add (EInt 4, EInt 5));; (* senza funzioni *)
pval p1;;

let p2 = Prog ([Fun ("succ", "x", Add (Den "x", EInt 1))], Add (App ("succ", EInt 4), EInt 5));; (* funzione non ricorsiva: succ *)
pval p2;;

let p3 = (* funzione ricorsiva: triangolare *) 
    Prog ([Fun ("tria", "x", Ifz (Eq (Den "x", EInt 0), EInt 5, Add (Den "x", App ("tria", Sub (Den "x", EInt 1)))))], App ("tria", EInt 4));;
pval p3;;

let p4 = (* funzione ricorsitva: fattoriale *)     
    Prog ([Fun ("fact", "x", Ifz (Leq (Den "x", EInt 1), EInt 1, Mul (Den "x", App ("fact", Sub(Den "x", EInt 1)))))], App("fact", EInt 3));;
pval p4;;

(*  Fibonacci:
        fun sub1 (n) {- (n, 1)};
        fun fib (n) {if (= (n, 0) || = (n, 1)) then n else + (fib (sub1 (n)), fib (sub2 (n)))};
        fun sub2 (m) {sub1 (sub1 (m))};
        ⇒ fib (4) = 3 *)
let ptest = 
    Prog (
        [Fun ("sub1", "n", Sub (Den "n", EInt 1));
        Fun ("fib", "n", 
            Ifz (Or (Eq (Den "n", EInt 0), Eq (Den "n", EInt 1)), 
                (* then *) Den "n",
                (* else *) Add (App ("fib", App ("sub1", Den "n")), App ("fib", App ("sub2", Den "n")))));
        Fun ("sub2", "m", Sub (Den "m", EInt 2))],
        App ("fib", EInt 4));;
pval ptest;;