#!/bin/bash

echo ""
echo "Applying migration Document"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/document                        controllers.DocumentController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/document                        controllers.DocumentController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeDocument                  controllers.DocumentController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeDocument                  controllers.DocumentController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "document.title = document" >> ../conf/messages.en
echo "document.heading = document" >> ../conf/messages.en
echo "document.documentType = documentType" >> ../conf/messages.en
echo "document.reference = reference" >> ../conf/messages.en
echo "document.checkYourAnswersLabel = Document" >> ../conf/messages.en
echo "document.error.documentType.required = Enter documentType" >> ../conf/messages.en
echo "document.error.reference.required = Enter reference" >> ../conf/messages.en
echo "document.error.documentType.length = documentType must be 2 characters or less" >> ../conf/messages.en
echo "document.error.reference.length = reference must be 35 characters or less" >> ../conf/messages.en
echo "document.documentType.change.hidden = documentType" >> ../conf/messages.en
echo "document.reference.change.hidden = reference" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDocumentUserAnswersEntry: Arbitrary[(DocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DocumentPage.type]";\
    print "        value <- arbitrary[Document].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDocumentPage: Arbitrary[DocumentPage.type] =";\
    print "    Arbitrary(DocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDocument: Arbitrary[Document] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        documentType <- arbitrary[String]";\
    print "        reference <- arbitrary[String]";\
    print "      } yield Document(documentType, reference)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration Document completed"
