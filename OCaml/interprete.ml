type ide = string;; (* identificatore di una variabile *)

type label = Lab of string;; (* per i record *)

type exp = (* albero di sintassi astratta del linguaggio AST *)
    | ETrue
    | EFalse
    | EInt of int
    | Den of ide
    | Add of exp * exp
    | Sub of exp * exp
    | Times of exp * exp
    | IsZero of exp
    | Equals of exp * exp
    | IfThenElse of exp * exp * exp
    | Let of ide * exp * exp
    | LetRec of ide * ide * exp * exp
    | Fun of ide * exp
    | Apply of exp * exp
    (* record *)
    | ERecord of (label * exp) list
    | Select of exp * label
    (* funzioni a dominio finito, es 9/12/2020 *)
    | DFun of ide * exp * exp list;; (* exp list è il dominio *)

type 'a env = (string * 'a) list;; (* ambiente polimorfo *)

type evT = (* valori a runtime *)
    | Unbound
    | Int of int
    | Bool of bool
    | Closure of ide * exp * evT env
    | RecClosure of ide * ide * exp * evT env
    | DFunClosure of ide * exp * evT list * evT env
    | Record of (label * evT) list;;

type typeTag =
    | IntTag
    | BoolTag;;

exception TypeError;;
exception TypeMismatch;;
exception FieldNotFound;;
exception ApplyError;;

(* --- *)

let empty_env : evT env = [("", Unbound)];;

let bind (env : evT env) (x : ide) (v : evT) = (x, v)::env;; (* aggiunge un nuovo binding a un ambiente esistente *) 

let rec lookup (env : evT env) (x : ide) = (* cerca il valore della variabile identificata da x nell'ambiente *)
    match env with
    | [] -> Unbound
    | (a, v)::_ when x = a -> v
    | _::t -> lookup t x;;

let rec lookup_record (body : (label * evT) list) (lab : label) : evT =
    match body with
    | [] -> raise FieldNotFound
    | (lab1, value)::rs -> 
        if lab = lab1 then value else lookup_record rs lab;; 

let rec typecheck (tag : typeTag) (v : evT) = (* typechecker dinamico *)
    match tag with 
    | IntTag ->
        begin match v with
        | Int _ -> true
        | _ -> false end
    | BoolTag -> 
        begin match v with
        | Bool _ -> true
        | _  -> false end;;

(* --- *)

let int_add (n : evT) (m : evT) : evT =
    match typecheck IntTag n, typecheck IntTag m, n, m with
    | true, true, Int a, Int b -> Int (a + b)
    | _, _, _, _ -> raise TypeError;;
  
let int_sub (n : evT) (m : evT) : evT =
    match typecheck IntTag n, typecheck IntTag m, n, m with
    | true, true, Int a, Int b -> Int (a - b)
    | _, _, _, _ -> raise TypeError;;
  
let int_times (n : evT) (m : evT) : evT =
    match typecheck IntTag n, typecheck IntTag m, n, m with
    | true, true, Int a, Int b -> Int (a * b)
    | _, _, _, _ -> raise TypeError;;
  
let is_zero (n : evT) : evT =
    match typecheck IntTag n, n with
    | true, Int a -> Bool (a = 0)
    | _, _ -> raise TypeError;;
  
let int_equals (v1 : evT) (v2 : evT) : evT =
    match typecheck IntTag v1, typecheck IntTag v2, v1, v2 with
    | true, true, Int n, Int m -> Bool (n = m)
    | _, _, _, _ -> raise TypeError;;

(* --- *)

let rec eval (e : exp) (env : evT env) : evT = (* interprete *)
    match e with
    | EInt n -> Int n
    | ETrue -> Bool true
    | EFalse -> Bool false
    | Den id -> lookup env id
    | Add (e1, e2) -> int_add (eval e1 env) (eval e2 env)
    | Sub (e1, e2) -> int_sub (eval e1 env) (eval e2 env)
    | Times (e1, e2) -> int_times (eval e1 env) (eval e2 env)
    | IsZero e -> is_zero (eval e env)
    | Equals (e1, e2) -> int_equals (eval e1 env) (eval e2 env)
    | IfThenElse (cond, e1, e2) ->
        let evaledCond = eval cond env in
            begin match typecheck BoolTag evaledCond, evaledCond with
            | true, Bool true -> eval e1 env
            | true, Bool false -> eval e2 env
            | _ -> raise TypeError end
    | Let (id, e1, e2) -> eval e2 (bind env id (eval e1 env)) (* LIFO *)
    | LetRec (funId, param, funBody, letBody) ->
        let recClos = RecClosure (funId, param, funBody, env) in
            let bindEnv = bind env funId recClos in
                eval letBody bindEnv
    | Fun (id, body) -> Closure (id, body, env)
    | DFun (id, body, domain) -> DFunClosure (id, body, eval_list domain env, env) (* funzioni a dominio finito *)
    | Apply (Den funName, arg) ->
        let funClosure = lookup env funName in
            begin match funClosure with
                | Closure (param, funBody, funDeclEnv) -> 
                    let actualVal = eval arg env in
                        let actualEnv = bind funDeclEnv param actualVal in
                            eval funBody actualEnv
                | RecClosure (funId, param, funBody, funDeclEnv) ->
                    let actualVal = eval arg env in
                        let recEnv = bind funDeclEnv funName funClosure in
                            let actualEnv = bind recEnv param actualVal in
                                eval funBody actualEnv
                | DFunClosure (param, funBody, funDomain, funDeclEnv) ->
                    let actualVal = eval arg env in
                        if List.mem actualVal funDomain (* member *)
                        then eval funBody (bind funDeclEnv param actualVal) else Unbound
                | _ -> raise ApplyError end
    | Apply (_, _) -> raise ApplyError
    (* record *)
    | ERecord body -> Record (eval_record body env)
    | Select (e, lab) ->
        begin match (eval e env) with
        | Record body -> lookup_record body lab
        | _ -> raise TypeMismatch end

and eval_record (body : (label * exp) list) (env : evT env) =
    match body with
    | [] -> []
    | (lab, e)::t -> (lab, (eval e env))::(eval_record t env)

and eval_list (l : exp list) (env : evT env) = (* per valutare il dominio delle DFun *)
    match l with
    | [] -> []
    | e::t -> (eval e env)::(eval_list t env);;
