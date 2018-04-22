(ns assets.handler
  (:require [assets.pages :as pages]
            [assets.files :as files]
            [ring.util.response :as res]
            [ring.util.mime-type :as mime]
            [bidi.ring :refer [make-handler]]
            [bidi.bidi :refer [path-for]]
            [clojure.string :refer [split join]]
            [clj-http.client :as client]
            [clojure.java.io :as io])
  (:import [java.io File]))

(def routes)

(defn index-handler [request]
  (let [files (files/list-files)
        sorted-files (reverse (sort-by :last-modified files))]
    (res/response
     (pages/index-page sorted-files))))

(defn -url-file-elems [url]
  (let [url-elems (split url #"/")
        filename (last url-elems)
        filename-elems (split filename #"\.")
        total-elems (count filename-elems)]
    (cond
      (> total-elems 2) (list (join "." (butlast filename-elems)) (last filename-elems))
      (= total-elems 1) (conj filename-elems "")
      :else filename-elems)))

(defn -fetch-url!
  [url]
  (let [req (client/get url {:as :byte-array :throw-exceptions false})]
    (if (= (:status req) 200)
      (:body req))))

(defn -save-url!
  [url]
  (let [content (-fetch-url! url)
        [prefix suffix] (-url-file-elems url)]
    (if content
      (let [tmp (File/createTempFile prefix suffix)]
        (io/copy content tmp)
        {:filename (format "%s.%s" prefix suffix)
         :tempfile tmp
         :content-type (mime/ext-mime-type url)}))))

(defn -handle-upload-param! [params]
  (if-let [url (get params "url")]
    (-save-url! url)
    (get params "asset")))

(defn upload-handler [request]
  (let [asset (-handle-upload-param! (:params request))]
    (files/upload-file (:filename asset)
                       (:tempfile asset)
                       (:content-type asset)))
  (res/redirect (path-for routes index-handler)))

(def routes ["/" {"" index-handler
                  "upload" upload-handler}])

(def handler (make-handler routes))
