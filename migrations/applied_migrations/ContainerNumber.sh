#!/bin/bash

echo ""
echo "Applying migration ContainerNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/containerNumber                        controllers.ContainerNumberController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/containerNumber                        controllers.ContainerNumberController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeContainerNumber                  controllers.ContainerNumberController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeContainerNumber                  controllers.ContainerNumberController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "containerNumber.title = containerNumber" >> ../conf/messages.en
echo "containerNumber.heading = containerNumber" >> ../conf/messages.en
echo "containerNumber.checkYourAnswersLabel = containerNumber" >> ../conf/messages.en
echo "containerNumber.error.required = Enter containerNumber" >> ../conf/messages.en
echo "containerNumber.error.length = ContainerNumber must be 20 characters or less" >> ../conf/messages.en
echo "containerNumber.change.hidden = ContainerNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContainerNumberUserAnswersEntry: Arbitrary[(ContainerNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ContainerNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContainerNumberPage: Arbitrary[ContainerNumberPage.type] =";\
    print "    Arbitrary(ContainerNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ContainerNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ContainerNumber completed"
