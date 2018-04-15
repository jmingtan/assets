(ns repl
  (:require [clojure.tools.nrepl.server :as nrepl-server]
            [cider.nrepl :refer [cider-nrepl-handler]]
            [rebel-readline.main :as rebel]))

(defn -main []
  (println "nrepl server at port 7888")
  (nrepl-server/start-server :port 7888 :handler cider-nrepl-handler)
  (rebel/-main)
  (System/exit 0))
