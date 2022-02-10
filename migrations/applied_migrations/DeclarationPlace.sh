#!/bin/bash

echo ""
echo "Applying migration DeclarationPlace"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/declarationPlace                        controllers.DeclarationPlaceController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/declarationPlace                        controllers.DeclarationPlaceController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeDeclarationPlace                  controllers.DeclarationPlaceController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeDeclarationPlace                  controllers.DeclarationPlaceController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "declarationPlace.title = declarationPlace" >> ../conf/messages.en
echo "declarationPlace.heading = declarationPlace" >> ../conf/messages.en
echo "declarationPlace.checkYourAnswersLabel = declarationPlace" >> ../conf/messages.en
echo "declarationPlace.error.required = Enter declarationPlace" >> ../conf/messages.en
echo "declarationPlace.error.length = DeclarationPlace must be 35 characters or less" >> ../conf/messages.en
echo "declarationPlace.change.hidden = DeclarationPlace" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeclarationPlaceUserAnswersEntry: Arbitrary[(DeclarationPlacePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DeclarationPlacePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeclarationPlacePage: Arbitrary[DeclarationPlacePage.type] =";\
    print "    Arbitrary(DeclarationPlacePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DeclarationPlacePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DeclarationPlace completed"
