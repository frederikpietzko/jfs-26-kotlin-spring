<template>
  <div class="slidev-layout default">
    <slot />
    <div class="jetbrains-logo-footer">
      <img src="/assets/jetbrains.svg" alt="JetBrains" class="h-8" />
    </div>
  </div>
</template>

<style scoped>
.jetbrains-logo-footer {
  position: absolute;
  bottom: 20px;
  right: 20px;
  z-index: 40;
}
</style>
