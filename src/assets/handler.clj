(ns assets.handler
  (:require [assets.pages :as pages]
            [assets.files :as files]
            [ring.util.response :as res]
            [bidi.ring :refer [make-handler]]
            [bidi.bidi :refer [path-for]]))

(def routes)

(defn index-handler [request]
  (res/response
   (pages/index-page (reverse (files/list-files)))))

(defn upload-handler [request]
  (let [asset (-> (:params request)
                  (get "asset"))]
    (files/upload-file (:filename asset)
                       (:tempfile asset)
                       (:content-type asset)))
  (res/redirect (path-for routes index-handler)))

(def routes ["/" {"" index-handler
                  "upload" upload-handler}])

(def handler (make-handler routes))
