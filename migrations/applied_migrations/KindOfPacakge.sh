#!/bin/bash

echo ""
echo "Applying migration KindOfPackage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/kindOfPackage                        controllers.KindOfPackageController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/kindOfPackage                        controllers.KindOfPackageController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeKindOfPackage                  controllers.KindOfPackageController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeKindOfPackage                  controllers.KindOfPackageController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "kindOfPackage.title = kindOfPackage" >> ../conf/messages.en
echo "kindOfPackage.heading = kindOfPackage" >> ../conf/messages.en
echo "kindOfPackage.checkYourAnswersLabel = kindOfPackage" >> ../conf/messages.en
echo "kindOfPackage.error.required = Enter kindOfPackage" >> ../conf/messages.en
echo "kindOfPackage.error.length = KindOfPackage must be 2 characters or less" >> ../conf/messages.en
echo "kindOfPackage.change.hidden = KindOfPackage" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryKindOfPackageUserAnswersEntry: Arbitrary[(KindOfPackagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[KindOfPackagePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryKindOfPackagePage: Arbitrary[KindOfPackagePage.type] =";\
    print "    Arbitrary(KindOfPackagePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(KindOfPackagePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration KindOfPackage completed"
