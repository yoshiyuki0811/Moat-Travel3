const stripe = Stripe('pk_test_51TYzfFBqXi1SxQtKRQZ6b9Ile778KeA0vlyIzOMSSxyxrESz4onfiRy7UKt7Pviy2BXmuzkH1bQJxdXfP5SbkR6F00vYk7heoM');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});