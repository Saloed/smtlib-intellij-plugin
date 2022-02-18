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

(declare-fun x () Bool)

(check-sat)
(get-model)
(get-info :reason-unknown)


