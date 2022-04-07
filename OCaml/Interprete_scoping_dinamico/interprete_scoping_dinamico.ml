type ide = string;; (* identificatore di una variabile *)

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
    | LessThan of exp * exp (* < *)
    | IfThenElse of exp * exp * exp
    | Let of ide * exp * exp
    | Fun of ide * exp
    | Apply of exp * exp
    (* funzioni a dominio finito *)
    | DFun of ide * exp * exp list (* exp list è il dominio *)
    (* funzioni con più parametri *)
    | ParamsFun of ide list * exp (* i parametri sono gli elementi della lista *)
    | ApplyParams of exp * exp list
    (* iterate *)
    | Iterate of exp * exp * exp;;

type 't env = (string * 't) list;; (* ambiente polimorfo *)

type evT = (* valori a runtime *)
    | Unbound
    | Int of int
    | Bool of bool
    | FunVal of ide * exp
    | DFunVal of ide * exp * evT list
    | ParamsFunVal of ide list * exp;;

type typeTag =
    | IntTag
    | BoolTag;;

exception InvalidArgument;;
exception TypeError;;
exception ApplyError;;

let empty_env : evT env = [("", Unbound)];;

let bind (env : evT env) (x : ide) (v : evT) = (x, v)::env;; (* aggiunge un nuovo binding a un ambiente esistente *) 

let rec bind_list (env : evT env) (idlist : ide list) (vlist : evT list) = (* serve per ParamsFun *)
    match (idlist, vlist) with
    | ([], []) -> env
    | (id::t1, v::t2) -> bind_list (bind env id v) t1 t2
    | _ -> failwith "bind_list error";;

let rec lookup (env : evT env) (x : ide) = (* cerca il valore della variabile identificata da x nell'ambiente *)
    match env with
    | [] -> Unbound
    | (a, v)::_ when x = a -> v
    | _::t -> lookup t x;;

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

let int_lessThan (v1 : evT) (v2 : evT) : evT =
    match typecheck IntTag v1, typecheck IntTag v2, v1, v2 with
    | true, true, Int n, Int m -> Bool (n < m)
    | _, _, _, _ -> raise TypeError;;

let from_evT_to_exp (v : evT) : exp =
    match v with
    | Int u -> EInt u
    | Bool u -> if u = true then ETrue else EFalse
    | _ -> failwith "from_evT_to_exp error";;

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
    | LessThan (e1, e2) -> int_lessThan (eval e1 env) (eval e2 env)
    | IfThenElse (cond, e1, e2) ->
        let evaledCond = eval cond env in
            begin match typecheck BoolTag evaledCond, evaledCond with
            | true, Bool true -> eval e1 env
            | true, Bool false -> eval e2 env
            | _ -> raise TypeError end
    | Let (id, e1, e2) -> eval e2 (bind env id (eval e1 env)) (* LIFO *)
    | Fun (id, body) -> FunVal (id, body)
    (* funzioni a dominio finito *)
    | DFun (id, body, domain) -> DFunVal (id, body, eval_list domain env)
    | Apply (Den funName, arg) ->
        let funClosure = lookup env funName in
            begin match funClosure with
            | FunVal (param, funBody) ->
                let actualVal = eval arg env in
                    eval funBody (bind env param actualVal)
            | DFunVal (param, funBody, funDomain) ->
                let actualVal = eval arg env in
                    if List.mem actualVal funDomain (* member *)
                        then eval funBody (bind env param actualVal) else Unbound
            | _ -> raise ApplyError end
    | Apply (_, _) -> raise InvalidArgument
    (* funzioni con più parametri *)
    | ParamsFun (idlist, body) -> ParamsFunVal (idlist, body)
    | ApplyParams (funName, args) ->
        let funClosure = eval funName env in
            begin match funClosure with
            | ParamsFunVal (params, funBody) ->
                let actualVals = eval_list args env in
                    eval funBody (bind_list env params actualVals)
            | _ -> raise ApplyError end
    (* iterate *)
    | Iterate (Den funName, arg, n) ->
        let m = from_evT_to_exp (int_sub (eval n env) (Int 1)) in
            let res = Apply (Den funName, arg) in
                if from_evT_to_exp (int_equals (eval m env) (Int 0)) = ETrue
                    then eval res env else eval (Iterate (Den funName, res, m)) env
    | Iterate (_, _, _) -> raise InvalidArgument

and eval_list (l : exp list) (env : evT env) = (* per valutare il dominio delle DFun *)
    match l with
    | [] -> []
    | e::t -> (eval e env)::(eval_list t env);;