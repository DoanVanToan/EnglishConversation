gem install chatwork
ruby scripts/chatwork.rb "${ROOM_ID}" "${CI_PULL_REQUEST}" "${TEAM}" "${CIRCLE_PR_USERNAME}" "${CIRCLE_PR_REPONAME}"
