<script setup>
import { ref, onMounted } from 'vue'

const question = ref(null)
const selectedAnswer = ref(null)
const answerWasCorrect = ref(null)
const wrongAnswers = ref([])

async function fetchQuestion() {
  selectedAnswer.value = null
  answerWasCorrect.value = null

  const response = await fetch('http://localhost:8080/questions')
  const data = await response.json()
  question.value = data
  console.log(question.value)
}

async function checkAnswer(questionId, answer) {
  selectedAnswer.value = answer
  const response = await fetch('http://localhost:8080/checkanswers', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      questionId: questionId,
      answer: answer
    })
  })
  const data = await response.json()

  if (data.answerWasCorrect) {
    answerWasCorrect.value = true
  } else {
    wrongAnswers.value.push(answer)
  }
}

onMounted(() => {
  fetchQuestion()
})

</script>

<template>
  <div class="app">
    <div v-if="question">
      <p v-html="question.question"></p>
      <div class="answers">
        <button v-for="answer in question.answers" :key="answer" @click="checkAnswer(question.id, answer)" :class="{
          correct: selectedAnswer === answer && answerWasCorrect === true,
          incorrect: wrongAnswers.includes(answer)
        }" :disabled="answerWasCorrect === true || wrongAnswers.includes(answer)">
          {{ answer }}
        </button>
      </div>
      <button v-if="answerWasCorrect" @click="fetchQuestion">
        Next Question
      </button>
    </div>

    <div v-else>
      <p>Loading...</p>
    </div>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  background-color: #121212;
  color: white;
}

.answers {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

button {
  padding: 10px;
  background: #2a2a2a;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background: #3a3a3a;
}

button.correct {
  background: #1f7a3a;
}

button.incorrect {
  background: #8a2424;
}
</style>
