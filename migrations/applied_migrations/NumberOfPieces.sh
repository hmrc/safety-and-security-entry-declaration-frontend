#!/bin/bash

echo ""
echo "Applying migration NumberOfPieces"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/numberOfPieces                  controllers.NumberOfPiecesController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/numberOfPieces                  controllers.NumberOfPiecesController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNumberOfPieces                        controllers.NumberOfPiecesController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNumberOfPieces                        controllers.NumberOfPiecesController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "numberOfPieces.title = NumberOfPieces" >> ../conf/messages.en
echo "numberOfPieces.heading = NumberOfPieces" >> ../conf/messages.en
echo "numberOfPieces.checkYourAnswersLabel = NumberOfPieces" >> ../conf/messages.en
echo "numberOfPieces.error.nonNumeric = Enter your numberOfPieces using numbers" >> ../conf/messages.en
echo "numberOfPieces.error.required = Enter your numberOfPieces" >> ../conf/messages.en
echo "numberOfPieces.error.wholeNumber = Enter your numberOfPieces using whole numbers" >> ../conf/messages.en
echo "numberOfPieces.error.outOfRange = NumberOfPieces must be between {0} and {1}" >> ../conf/messages.en
echo "numberOfPieces.change.hidden = NumberOfPieces" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNumberOfPiecesUserAnswersEntry: Arbitrary[(NumberOfPiecesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NumberOfPiecesPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNumberOfPiecesPage: Arbitrary[NumberOfPiecesPage.type] =";\
    print "    Arbitrary(NumberOfPiecesPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NumberOfPiecesPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NumberOfPieces completed"
