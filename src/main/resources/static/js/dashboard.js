// A simple interactive element: when you hover over balance card show more detail
document.addEventListener('DOMContentLoaded', () => {
  const balanceCard = document.querySelector('.balance-card');
  balanceCard.addEventListener('mouseover', () => {
    balanceCard.style.backgroundColor = '#eaf6ff';
  });
  balanceCard.addEventListener('mouseout', () => {
    balanceCard.style.backgroundColor = 'white';
  });
});
