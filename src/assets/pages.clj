(ns assets.pages
  (:require [hiccup.page :as page]))

(defn index-page [files]
  (page/html5
   [:html
    [:head
     [:title "Asset Uploader"]]
    [:body
     [:h1 "Upload Asset"]
     [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
      [:div [:input {:name "asset" :type "file"}]]
      [:input {:type "submit" :value "Upload"}]]
     [:h1 "Asset List"]
     [:ol
      (for [file files]
        [:li
         [:a {:href (:url file)} (:name file)]])]]]))
