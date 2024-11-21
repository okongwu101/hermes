(ns com.eldrix.hermes.snomed-test
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.string :as str]
            [clojure.test :refer [deftest is run-tests testing]]
            [com.eldrix.hermes.importer :as importer]
            [com.eldrix.hermes.rf2 :as rf2]
            [com.eldrix.hermes.snomed :as snomed]
            [com.eldrix.hermes.verhoeff :as verhoeff])
  (:import [java.time LocalDate]
           (com.eldrix.hermes.snomed AssociationRefsetItem AttributeValueRefsetItem ComplexMapRefsetItem ExtendedMapRefsetItem LanguageRefsetItem OWLExpressionRefsetItem SimpleMapRefsetItem)))

(stest/instrument)

(deftest test-filenames
  (let [examples (slurp (io/resource "example-snomed-file-list.txt"))
        parsed (map #(hash-map :filename % :data (snomed/parse-snomed-filename %)) (str/split examples #"\n"))]
    (doseq [f parsed]
      (is (some? (:data f)) (str "couldn't parse filename" f)))))

(deftest test-filenames2
  (let [p (snomed/parse-snomed-filename "der2_cRefset_AttributeValueSnapshot_INT_20180131.txt")]
    (is (= "AttributeValue" (:summary p)))
    (is (= "Snapshot" (:release-type p)))
    (is (nil? (:language-code p))))
  (let [p (snomed/parse-snomed-filename "sct2_Description_Snapshot-en_INT_20180131.txt")]
    (is (= "Snapshot" (:release-type p)))
    (is (= "en" (:language-code p)))))

(deftest test-refset-filename-pattern
  (is (= '(1 2 3) (snomed/parse-fields "iii" ["1" "2" "3"])))
  (is (= '("1" 2 3) (snomed/parse-fields "sii" ["1" "2" "3"])))
  (is (= '("1" 2 3 "4") (snomed/parse-fields "sics" ["1" "2" "3" "4"])))
  (is (thrown? Exception (snomed/parse-fields "a" ["1"])))
  (is (thrown? Exception (snomed/parse-fields "iii" ["1" "2"]))))

(deftest test-partition
  (is (= :info.snomed/Concept (snomed/identifier->type 24700007)))
  (is (= :info.snomed/Concept (snomed/identifier->type " 24700007")))
  (is (= :info.snomed/Description (snomed/identifier->type 110017)))
  (is (= :info.snomed/Relationship (snomed/identifier->type 100022))))

(def core-examples
  [{:filename "sct2_Concept_Full_INT_20180131.txt"
    :type     :info.snomed/Concept
    :data     [["100005" "20020131" "0" "900000000000207008" "900000000000074008"]]}
   {:filename "sct2_Description_Full-en_INT_20180131.txt"
    :type     :info.snomed/Description
    :data     [["101013" "20020131" "1" "900000000000207008" "126813005" "en" "900000000000013009"
                "Neoplasm of anterior aspect of epiglottis" "900000000000020002"]]}
   {:filename "sct2_Relationship_Full_INT_20180131.txt"
    :type     :info.snomed/Relationship
    :data     [["100022" "20020131" "1" "900000000000207008" "100000000" "102272007" "0" "116680003" "900000000000011006" "900000000000451002"]]}])

(deftest test-valid?
  (let [ms (snomed/->Concept 24700007 (LocalDate/now) true 0 0)]
    (is (verhoeff/valid? (:id ms)))
    (is (= :info.snomed/Concept (snomed/identifier->type (:id ms))))))

(defn test-example [m]
  (let [t (:type m)
        r (snomed/parse-batch m)
        v (doall (map #(= t (snomed/identifier->type (:id %))) (:data r)))]
    (is (every? true? v))))

(deftest test-parsing
  (doall (map test-example core-examples)))

(def parse-unparse-tests
  [{:spec     :info.snomed/Concept
    :make-fn  snomed/map->Concept
    :parse-fn snomed/parse-concept}
   {:spec     :info.snomed/Description
    :make-fn  snomed/map->Description
    :parse-fn snomed/parse-description}
   {:spec     :info.snomed/Relationship
    :make-fn  snomed/map->Relationship
    :parse-fn snomed/parse-relationship}
   {:spec     :info.snomed/ConcreteValue
    :make-fn  snomed/map->ConcreteValue
    :parse-fn snomed/parse-concrete-value}
   {:spec     :info.snomed/SimpleRefset
    :make-fn  snomed/map->SimpleRefsetItem
    :parse-fn snomed/parse-simple-refset-item}
   {:spec     :info.snomed/AssociationRefset
    :make-fn  snomed/map->AssociationRefsetItem
    :parse-fn snomed/parse-association-refset-item}
   {:spec     :info.snomed/LanguageRefset
    :make-fn  snomed/map->LanguageRefsetItem
    :parse-fn snomed/parse-language-refset-item}
   {:spec     :info.snomed/RefsetDescriptorRefset
    :make-fn  snomed/map->RefsetDescriptorRefsetItem
    :parse-fn snomed/parse-refset-descriptor-item}
   {:spec     :info.snomed/SimpleMapRefset
    :make-fn  snomed/map->SimpleMapRefsetItem
    :parse-fn snomed/parse-simple-map-refset-item}
   {:spec     :info.snomed/ComplexMapRefset
    :make-fn  snomed/map->ComplexMapRefsetItem
    :parse-fn snomed/parse-complex-map-refset-item}
   {:spec     :info.snomed/ExtendedMapRefset
    :make-fn  snomed/map->ExtendedMapRefsetItem
    :parse-fn snomed/parse-extended-map-refset-item}
   {:spec     :info.snomed/AttributeValueRefset
    :make-fn  snomed/map->AttributeValueRefsetItem
    :parse-fn snomed/parse-attribute-value-refset-item}
   {:spec     :info.snomed/OWLExpressionRefset
    :make-fn  snomed/map->OWLExpressionRefsetItem
    :parse-fn snomed/parse-owl-expression-refset-item}
   {:spec     :info.snomed/MRCMDomainRefset
    :make-fn  snomed/map->MRCMDomainRefsetItem
    :parse-fn snomed/parse-mrcm-domain-refset-item}
   {:spec     :info.snomed/MRCMAttributeDomainRefset
    :make-fn  snomed/map->MRCMAttributeDomainRefsetItem
    :parse-fn snomed/parse-mrcm-attribute-domain-refset-item}
   {:spec     :info.snomed/MRCMAttributeRangeRefset
    :make-fn  snomed/map->MRCMAttributeRangeRefsetItem
    :parse-fn snomed/parse-mrcm-attribute-range-refset-item}
   {:spec     :info.snomed/MRCMModuleScopeRefset
    :make-fn  snomed/map->MRCMModuleScopeRefsetItem
    :parse-fn snomed/parse-mrcm-module-scope-refset-item}])

(deftest test-parse-unparse
  (doseq [{:keys [spec make-fn parse-fn]} parse-unparse-tests]
    (dorun (->> (gen/sample (s/gen spec) 4)
                (map make-fn)
                (map #(if (isa? spec :info.snomed/Refset)
                        (is (= % (parse-fn (rf2/pattern-for-refset-item spec %) (snomed/unparse %))))
                        (is (= % (parse-fn (snomed/unparse %))))))))))

(def refset-examples
  [{:filename "der2_Refset_SimpleFull_INT_20180131.txt"
    :type     :info.snomed/SimpleRefset
    :data     [["800aa109-431f-4407-a431-6fe65e9db160" "20170731" "1" "900000000000207008" "723264001" "731819006"]]}])

(deftest test-hierarchy
  (is (isa? :info.snomed/SimpleMapRefset :info.snomed/Refset))
  (is (isa? :info.snomed/Concept :info.snomed/Component)))

(def example-filenames
  [#_{:filename     "sct2_sRefset_OWLExpressionUKEDSnapshot_GB_20210512.txt"
      :component    "OWLExpressionRefset"
      :identifier   :info.snomed/OWLExpressionRefset
      :release-type "Snapshot"
      :content      "Refset"
      :summary      "OwlExpression"}
   {:filename          "der2_cRefset_AssociationSnapshot_GB1000000_20180401.txt"
    :format            "2"
    :content-subtype   "AssociationSnapshot"
    :namespace-id      "1000000"
    :content           "Refset"
    :country-code      "GB"
    :type              "der"
    :component         "AssociationRefset"
    :summary           "Association"
    :file-type         "der2"
    :release-type      "Snapshot"
    :identifier        :info.snomed/AssociationRefset
    :file-extension    "txt"
    :content-type      "cRefset"
    :country-namespace "GB1000000"
    :pattern           "c"}
   {:filename          "sct2_Concept_UKEDSnapshot_GB_20210512.txt"
    :format            "2"
    :content-subtype   "Snapshot"
    :namespace-id      ""
    :content           "Concept"
    :doc-status        nil
    :country-code      "GB"
    :type              "sct"
    :component         "Concept"
    :file-type         "sct2"
    :release-type      "Snapshot"
    :language-code     nil
    :identifier        :info.snomed/Concept
    :file-extension    "txt"
    :content-type      "Concept"
    :country-namespace "GB"
    :pattern           ""}
   {:filename          "sct2_Concept_Snapshot_GB1000000_20180401.txt"
    :format            "2"
    :content-subtype   "Snapshot"
    :namespace-id      "1000000",
    :content           "Concept",
    :doc-status        nil,
    :country-code      "GB",
    :type              "sct",
    :component         "Concept",
    :summary           "",
    :status            "",
    :file-type         "sct2",
    :release-type      "Snapshot",
    :language-code     nil,
    :identifier        :info.snomed/Concept,
    :file-extension    "txt",
    :content-type      "Concept",
    :country-namespace "GB1000000",
    :pattern           ""}
   {:filename          "der2_Refset_cardiologySnapshot_IN1000189_20210806.txt"
    :format            "2"
    :content-subtype   "Snapshot"
    :namespace-id      "1000189"
    :content           "Refset"
    :doc-status        nil
    :country-code      "IN"
    :type              "der"
    :component         "Refset"
    :summary           ""
    :status            ""
    :file-type         "der2"
    :release-type      "Snapshot"
    :language-code     nil
    :identifier        :info.snomed/SimpleRefset
    :file-extension    "txt"
    :content-type      "Refset"
    :country-namespace "IN1000189"
    :pattern           ""}
   {:filename          "der2_sRefset_VTMSpainDrugSnapshot_es-ES_ES_20211001.txt"
    :format            "2"
    :content-subtype   "Snapshot"
    :namespace-id      nil
    :content           "Refset"
    :doc-status        nil
    :country-code      "ES"
    :type              "der"
    :component         "Refset"
    :summary           ""
    :status            ""
    :file-type         "der2"
    :release-type      "Snapshot"
    :language-code     "es-ES"
    :identifier        :info.snomed/SimpleRefset
    :file-extension    "txt"
    :content-type      "Refset"
    :country-namespace nil
    :pattern           "s"}
   {:filename   "der2_sRefset_SimpleMapSnapshot_INT_20210131.txt"
    :format     "2"
    :identifier :info.snomed/SimpleMapRefset}])

;; Since May 2021, the UK has taken the egregious step of shoehorning a random
;; identifier into the filename to say where the file used to exist!
;; On the face of it, a callous disregard for good data design sensibility.
(deftest test-uk-filenames
  (doseq [example example-filenames]
    (let [snofile (snomed/parse-snomed-filename (:filename example))]
      (is (= (:identifier example) (:identifier snofile)) (str "Failed to parse filename: " (:filename example)))
      (is (:parser snofile)))))

(def reification-refset-items
  [{:attributes [449608002 900000000000533001] :class AssociationRefsetItem
    :fields     [1] :expected {:targetComponentId 1}}
   {:attributes [449608002 900000000000511003] :class LanguageRefsetItem
    :fields     [1] :expected {:acceptabilityId 1}}
   {:attributes [900000000000500006 900000000000505001] :class SimpleMapRefsetItem
    :fields     [1] :expected {:mapTarget 1}}
   {:attributes [900000000000500006 900000000000501005 900000000000502003 900000000000503008 900000000000504002 900000000000505001 1193546000 609330002]
    :class      ExtendedMapRefsetItem :fields [1 2 3 4 5 6 7]
    :expected   {:mapGroup 1, :mapPriority 2, :mapRule 3, :mapAdvice 4, :mapTarget 5, :correlationId 6, :mapCategoryId 7}}
   {:attributes [900000000000500006 900000000000501005 900000000000502003 900000000000503008 900000000000504002 900000000000505001 1193546000]
    :class      ComplexMapRefsetItem :fields [1 2 3 4 5 6]
    :expected   {:mapGroup 1, :mapPriority 2, :mapRule 3, :mapAdvice 4, :mapTarget 5, :correlationId 6}}
   {:attributes [449608002 900000000000491004] :class AttributeValueRefsetItem
    :fields     [1] :expected {:valueId 1}}
   {:attributes [449608002 762677007] :class OWLExpressionRefsetItem
    :fields     [1] :expected {:owlExpression 1}}])

(deftest test-refset-reification
  (doseq [{:keys [attributes fields expected] clazz :class} reification-refset-items]
    (let [item (gen/generate (rf2/gen-simple-refset {:fields fields}))
          reifier (snomed/refset-reifier attributes)
          item' (reifier item)]
      (is (instance? clazz item'))
      (is (= (into {} (merge item expected {:fields []})) (into {} item'))))))

(deftest test-release-metadata
  (testing "Release metadata"
    (let [{:keys [effectiveTime modules]} (importer/read-metadata (io/resource "example-release_package_information.json"))]
      (is (= (LocalDate/of 2022 8 3) effectiveTime))
      (is (= #{999000011000000103 999000031000000106} (set (keys modules)))))))

(def valid-identifiers
  "See https://confluence.ihtsdotools.org/display/DOCRELFMT/6.8+Example+SNOMED+CT+identifiers"
  [{:id 100005, :type :info.snomed/Concept}
   {:id 100014, :type :info.snomed/Description}
   {:id 100022, :type :info.snomed/Relationship}
   {:id 1290023401004, :type :info.snomed/Concept}
   {:id 1290023401015, :type :info.snomed/Description}
   {:id 9940000001029, :type :info.snomed/Relationship}
   {:id 11000001102, :type :info.snomed/Concept, :ns "1000001"}
   {:id 10989121108, :type :info.snomed/Concept, :ns "0989121"}
   {:id 1290989121103, :type :info.snomed/Concept, :ns "0989121"}
   {:id 1290000001117, :type :info.snomed/Description, :ns "0000001"}
   {:id 9940000001126, :type :info.snomed/Relationship, :ns "0000001"}
   {:id 999999990989121104, :type :info.snomed/Concept, :ns "0989121"}])

(defn make-invalid-identifiers
  "Given a valid identifier, make a sequence of invalid identifiers."
  [id]
  (let [s (str id), l (.length s), e (dec l), c (.charAt s e), prefix (subs s 0 e)]
    (map #(str prefix %) (set/difference (set (apply str (range 10))) #{c}))))

(deftest test-identifiers
  (doseq [{id :id component-type :type nspace :ns} valid-identifiers]
    (is (verhoeff/valid? id))
    (is (not-any? verhoeff/valid? (make-invalid-identifiers id)))
    (is (= component-type (snomed/identifier->type id)))
    (is (= nspace (snomed/identifier->namespace id)))))

(def case-sensitivity-examples
  [{:term "Fracture of tibia" :caseSignificanceId snomed/EntireTermCaseInsensitive :expected "fracture of tibia"}
   {:term "pH measurement" :caseSignificanceId snomed/EntireTermCaseSensitive :expected "pH measurement"}
   {:term "Born in Australia" :caseSignificanceId snomed/OnlyInitialCharacterCaseInsensitive :expected "born in Australia"}])

(deftest test-lowercase
  (doseq [{:keys [expected] :as d} case-sensitivity-examples]
    (let [d' (gen/generate (rf2/gen-description d))]
      (is (= expected (snomed/term->lowercase d'))))))

(comment
  (run-tests)
  (snomed/parse-snomed-filename "sct2_sRefset_OWLExpressionUKEDSnapshot_GB_20210512.txt")
  (snomed/parse-snomed-filename "der2_cRefset_AssociationSnapshot_GB1000000_20180401.txt")
  (snomed/parse-snomed-filename "sct2_Concept_UKEDSnapshot_GB_20210512.txt")
  (snomed/parse-snomed-filename "sct2_Concept_Snapshot_GB1000000_20180401.txt")
  (snomed/parse-snomed-filename "der2_sRefset_SimpleMapSnapshot_INT_20210131.txt"))

