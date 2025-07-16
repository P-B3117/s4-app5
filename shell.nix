let
  nixpkgs = fetchTarball "https://github.com/NixOS/nixpkgs/tarball/nixos-unstable";
  pkgs = import nixpkgs { config = {}; overlays = []; };
in

pkgs.mkShellNoCC {
  packages = with pkgs; [
    jdk21
  ];


  shellHook = ''
      alias compile='javac *.java'
      alias build='jar cvfm AnalLex.jar manifest.txt *.class'
      alias run='java AnalLex.jar'
      alias clean='rm *.class'
  '';
}
