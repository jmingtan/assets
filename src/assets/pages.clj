(ns assets.pages
  (:require [hiccup.page :as page]))

(defn index-page [files]
  (page/html5
   [:html
    [:head
     [:title "Asset Uploader"]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.1/css/bulma.min.css"}]
     [:script {:defer "true" :src "https://use.fontawesome.com/releases/v5.0.7/js/all.js"}]]
    [:body
     [:section.section
      [:div.container
       [:div.columns
        [:div.column.is-one-third
         [:h1.title "Upload Asset"]
         [:section.section
           [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
            [:div.field
             [:label.label "File"]
             [:div.control
               [:input.input {:name "asset" :type "file"}]]]
            [:input.button.is-primary {:type "submit" :value "Upload from File"}]]]
         [:section.section
           [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
            [:div.field
             [:label.label "URL"]
             [:div.control
              [:input.input {:name "url" :type "text"}]]]
            [:input.button.is-primary {:type "submit" :value "Upload from URL"}]]]]
        [:div.column.is-one-fifth]
        [:div.column
         [:h1.title "Asset List"]
         [:div
          [:ol
           (for [file files]
             [:li
              [:a {:href (:url file)} (:name file)]])]]]]]]]]))
