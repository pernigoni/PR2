(*	
	ocamlc -o tutorial tutorial.ml
	ocamlrun tutorial
*)

print_string "Hello World!\n";;

let calculator (a : int) (op : char) (b : int) : int =
	if op = '+' then a + b
	else if op = '-' then a - b
	else if op = '*' then a * b
	else if op = '/' then a / b
	else if op = '%' then a mod b
	else 999;;

print_endline (
	string_of_int (calculator 10 '+' 5) ^ " " ^
	string_of_int (calculator 10 '-' 5) ^ " " ^
	string_of_int (calculator 10 '*' 5) ^ " " ^
	string_of_int (calculator 10 '/' 5) ^ " " ^
	string_of_int (calculator 10 '%' 5) ^ " " ^
	string_of_int (calculator 10 '^' 5));;

let is_zero (x : int) : string = 
	match x with
	| 0 -> "zero"
	| _ -> "non zero";;

print_endline ((is_zero (calculator 10 '+' 5)) ^ " - " ^ (is_zero (calculator 10 '%' 5)));;

let is_empty l : string =
	match l with
	| [] -> "empty"
	| h::t -> "not empty";;

print_endline ((is_empty []) ^ " - " ^ (is_empty [1; 2; 3]));;

let rec sum_l l : int =
	match l with
	| [] -> 0
	| h::t -> h + sum_l t;;

Printf.printf "%d\n" (sum_l [2; 3; 7; 1; 0; -4; 5]);;

(* PRL1 *)

let rec mcd a b =
	if a = b then a
	else if a > b then mcd (a - b) b
	else mcd a (b - a);;

Printf.printf "%d\n" (mcd 325884 8484848);;

let rec delete_last l =
	match l with
	| [] -> []
	| [_] -> []
	| x::y::ys -> x::delete_last (y::ys);;

let my_l = [10; 9; 8; 7; 6; 5; 4; 3; 2; 1];;
List.iter (Printf.printf "%d ") (delete_last my_l);;	print_string "\n";;	
List.iter (Printf.printf "%d ") my_l;; (* la lista non è cambiata perché è immutable come tutte le variabili in OCaml *)
print_string "\n";;

let reverse l = 
	let rec reverse_a l a = 
		match l with
		| [] -> a
		| x::xs -> reverse_a xs (x::a)
	in reverse_a l [];;

List.iter (Printf.printf "%d ") (reverse my_l);;	print_string "\n";;	

let print_int_pair p (x, y) = Printf.fprintf p "(%d, %d)" x y;;

let rec min_max l =
	match l with
	| [] -> (999, 999)
	| [x] -> (x, x)
	| x::y::ys ->
		let (m1, m2) = min_max (y::ys) in
			if x < m1 then (x, m2)
			else if x > m2 then (m1, x)
			else (m1, m2);;

Printf.printf "%a\n" print_int_pair (min_max my_l);;

let rec member l t =
	match l with
	| [] -> false
	| x::xs ->
		if t = x then true
		else member xs t;;

print_endline (string_of_bool (member my_l 77));;

(* Una funzione di ordine superiore ha come argomento o come risultato altre funzioni. *)

let even x = if x mod 2 = 0 then true else false;;

let incr x = x + 1;;

let rec forall p l = (* verifica che un predicato sia vero su tutti gli elementi della lista *)
	match l with
	| [] -> true
	| x::xs when not (p x) -> false
	| x::xs when p x -> forall p xs;;

print_endline ((string_of_bool (forall even my_l)) ^ " - " ^ (string_of_bool (forall even [6; 2; 78; 0])));;

let rec exists  p l = (* verifica che un predicato sia vero su almeno un elemento della lista *)
	match l with
	| [] -> false
	| x::xs when not (p x) -> exists p xs
	| x::xs when p x -> true;;

print_endline ((string_of_bool (exists even my_l)) ^ " - " ^ (string_of_bool (exists even [1; 3])));;

let my_l = [-2; 4; -3; 10; 11; 2; -1; -1];;

let f_1 x y = if x > 0 then x + y else y;;

Printf.printf "%d\n" (List.fold_right f_1 my_l 0);; (* somma dei valori positivi di una lista *)

let f_2 x y = if x > 0 then x::y else y;;

List.iter (Printf.printf "%d ") (List.fold_right f_2 my_l []);; (* lista dei soli elementi positivi di una lista *)
Printf.printf "\n";;

let filter p l = (* lista che contiene gli elementi di l su cui il predicato è vero *)
	let f x y =
		if p x then x::y else y
	in List.fold_right f l [];;

List.iter (Printf.printf "%d ") (filter even my_l);;	Printf.printf "\n";;

let map f l =
	let g x y = f x::y
	in List.fold_right g l [];;

List.iter (Printf.printf "%d ") (map incr my_l);;	Printf.printf "\n";;

let forall_foldr p l = 
	let f x y =
		if p x then y else false
	in List.fold_right f l true;;

let exists_foldr p l =
	let f x y =
		if p x then true else y
	in List.fold_right f l false;;

(* ALBERI BINARI *)

type 'a btree = Void | Node of 'a * 'a btree * 'a btree;;

let rec split l =
	match l with
	| [] -> ([], [])
	| [x] -> ([x], [])
	| x::y::ys ->
		let (l1, l2) = split ys
		in (x::l1, y::l2);;
(* 	val split : 'a list -> 'a list * 'a list = <fun>	*)

let rec build_btree l = 
	match l with
	| [] -> Void
	| x::xs ->
		let (l1, l2) = split xs
		in Node (x, build_btree l1, build_btree l2);;
(* 	
	val build_btree : 'a list -> 'a btree = <fun>
	# build_btree [3; 4; 5; 6];;
	- : int btree = Node (3, Node (4, Node (6, Void, Void), Void), Node (5, Void, Void))

			3
		4		5
	6

*)
