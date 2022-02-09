(set-logic HORN)
(set-option :timeout 100000)
(set-option :unsat_core true)
(set-option :fp.engine spacer)
(set-option :fp.generate_proof_trace true)
(set-option :fp.xform.inline_eager false)
(set-option :fp.xform.inline_linear false)
(set-option :fp.xform.compress_unbound false)
(set-option :fp.datalog.generate_explanations true)
(set-option :fp.datalog.similarity_compressor false)
(set-option :fp.datalog.unbound_compressor false)
(set-option :fp.datalog.subsumption false)
(set-option :fp.spacer.iuc.debug_proof true)
(set-option :fp.spacer.q3 false)
(set-option :fp.spacer.simplify_pob true)

(declare-fun function_argument_predicate__0 (Int Int Int (Array Int Int) Int Int Int Int) Bool)
(assert
        (forall
               (
               (arg$1 Int)
               (call__0__result Int)
               (%3 Int)
               (%0 Int)
               (call__1__result Int)
               (%8 Int)
               (%4 Int)
               (__property__org/jetbrains/research/kex/test/refinements/Inlining$SampleCls.x__0 (Array Int Int))
               (%1 Bool)
               (this Int)
               (<retval> Int)
               (%2 Int)
               (call__4__result Int)
               (%7 Bool)
               (call__3__result Int)
               (%6 Int)
               (%9 Int)
               (call__2__result Int)
               (arg$0 Int)
               (%5 Int)
               )
               (let
                   (
                   (a!1 (or
                           false
                           (and (and true (= %1 false)) (= %2 call__1__result) (= %4 %2))
                           (and (and true (= %1 true)) (= %3 call__2__result) (= %4 %3))
                        ))
                   (a!3 (and
                            true
                            (= %5 call__3__result)
                            (= %6 call__4__result)
                            (= %7 (>= %6 arg$0))
                        ))
                   (a!4 (<= (+ arg$0 (* (- 1) (select __property__org/jetbrains/research/kex/test/refinements/Inlining$SampleCls.x__0 %5))) 0))
                   (a!5 (= (>= (+ %6 (* (- 1) arg$0)) 0) false))
                   (a!6 (and
                            (>= arg$1 0)
                            (< arg$1 1024)
                            (>= %2 0)
                            (>= call__1__result 0)
                            (>= %4 0)
                            (>= %2 0)
                            (>= %3 0)
                            (>= call__2__result 0)
                            (>= %4 0)
                            (>= %3 0)
                        ))
                   )
                   (let
                       (
                       (a!2 (and
                                true
                                (not (= arg$1 0))
                                (= %0 call__0__result)
                                (= %1 (<= arg$0 %0))
                                a!1
                            ))
                       )
                       (let
                           (
                           (a!7 (and
                                    (or
                                       false
                                       (and a!2 a!3 (= %7 true) (= <retval> %4))
                                       (and a!2 a!3 (= %7 false) (= %8 1025) true (= %9 %8))
                                    )
                                    (=>
                                       (and true (= a!4 false))
                                       (and true (= %6 call__4__result) a!5)
                                    )
                                    a!6
                                    (and (>= %5 0) (>= call__3__result 0))
                                    (>= <retval> 0)
                                    (>= %4 0)
                                    a!6
                                    (and (>= %5 0) (>= call__3__result 0))
                                    (>= %8 0)
                                    (>= %9 0)
                                    (>= %8 0)
                                    (or false (and true (= %1 false)) (and true (= %1 true)))
                                    true
                                    (= %7 false)
                                ))
                           )
                           (=> a!7 (function_argument_predicate__0 arg$1 call__0__result call__1__result __property__org/jetbrains/research/kex/test/refinements/Inlining$SampleCls.x__0 this call__3__result call__2__result arg$0))
                       )
                   )
               )
        ))
(assert
        (forall
               (
               (arg$1 Int)
               (call__0__result Int)
               (%3 Int)
               (%0 Int)
               (call__1__result Int)
               (%8 Int)
               (%4 Int)
               (__property__org/jetbrains/research/kex/test/refinements/Inlining$SampleCls.x__0 (Array Int Int))
               (%1 Bool)
               (this Int)
               (<retval> Int)
               (%2 Int)
               (call__4__result Int)
               (%7 Bool)
               (call__3__result Int)
               (%6 Int)
               (%9 Int)
               (call__2__result Int)
               (arg$0 Int)
               (%5 Int)
               )
               (let
                   (
                   (a!1 (or
                           false
                           (and (and true (= %1 false)) (= %2 call__1__result) (= %4 %2))
                           (and (and true (= %1 true)) (= %3 call__2__result) (= %4 %3))
                        ))
                   (a!3 (and
                            true
                            (= %5 call__3__result)
                            (= %6 call__4__result)
                            (= %7 (>= %6 arg$0))
                        ))
                   (a!4 (<= (+ arg$0 (* (- 1) (select __property__org/jetbrains/research/kex/test/refinements/Inlining$SampleCls.x__0 %5))) 0))
                   (a!5 (= (>= (+ %6 (* (- 1) arg$0)) 0) false))
                   (a!6 (and
                            (>= arg$1 0)
                            (< arg$1 1024)
                            (>= %2 0)
                            (>= call__1__result 0)
                            (>= %4 0)
                            (>= %2 0)
                            (>= %3 0)
                            (>= call__2__result 0)
                            (>= %4 0)
                            (>= %3 0)
                        ))
                   )
                   (let
                       (
                       (a!2 (and
                                true
                                (not (= arg$1 0))
                                (= %0 call__0__result)
                                (= %1 (<= arg$0 %0))
                                a!1
                            ))
                       )
                       (let
                           (
                           (a!7 (and (or
                                        false
                                        (and a!2 a!3 (= %7 true) (= <retval> %4))
                                        (and a!2 a!3 (= %7 false) (= %8 1025) true (= %9 %8))
                                     ) (=>
                                          (and true (= a!4 false))
                                          (and true (= %6 call__4__result) a!5)
                                       ) a!6 (and
                                                 (>= %5 0) (>= call__3__result 0)
                                             ) (>= <retval> 0) (>= %4 0) a!6 (and (>= %5 0) (>= call__3__result 0)) (>= %8 0) (>= %9 0) (>= %8 0) (or false (and true (= %1 false)) (and true (= %1 true))) true (= %7 true) (or (function_argument_predicate__0 arg$1 call__0__result call__1__result __property__org/jetbrains/research/kex/test/refinements/Inlining$SampleCls.x__0 this call__3__result call__2__result arg$0))))
                           )
                           (=> a!7 false)
                       )
                   )
               )
        ))

(check-sat)
(get-model)
(get-info :reason-unknown)